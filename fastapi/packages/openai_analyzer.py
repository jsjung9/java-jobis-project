import json
import os

from openai import OpenAI, OpenAIError
from pydantic import ValidationError

from models import NewsAnalysis, NewsArticle


class AnalysisError(RuntimeError):
    pass


def analyze_news(
    news_articles: list[NewsArticle],
    *,
    client: OpenAI | None = None,
    model: str | None = None,
) -> NewsAnalysis:
    if not news_articles:
        return NewsAnalysis(keywords=[])

    api_key = os.getenv("OPENAI_API_KEY")
    if client is None and not api_key:
        raise AnalysisError("OPENAI_API_KEY가 설정되지 않았습니다.")

    openai_client = client or OpenAI(api_key=api_key, timeout=10, max_retries=1)
    selected_model = model or os.getenv("OPENAI_MODEL", "gpt-5-mini")

    try:
        response = openai_client.responses.create(
            model=selected_model,
            instructions=(
                "당신은 구직자가 기업을 이해하도록 돕는 분석가입니다. "
                "기사에 직접 근거한 구체적인 한국어 키워드만 반환하세요. "
                "회사명과 '시장', '회사', '팀' 같은 일반어는 제외하세요."
            ),
            input=_article_input(news_articles),
            text={
                "format": {
                    "type": "json_schema",
                    "name": "company_news_analysis",
                    "schema": NewsAnalysis.model_json_schema(),
                    "strict": True,
                }
            },
            max_output_tokens=800,
            store=False,
        )
        return NewsAnalysis.model_validate(json.loads(response.output_text))
    except (OpenAIError, ValidationError, json.JSONDecodeError, TypeError, AttributeError) as exc:
        raise AnalysisError("뉴스 분석 결과를 처리하지 못했습니다.") from exc


def _article_input(news_articles: list[NewsArticle]) -> str:
    sections = []
    for article in news_articles:
        sections.append(
            "\n".join(
                [
                    f"제목: {article.title}",
                    f"날짜: {article.date or '날짜 미상'}",
                    f"본문: {article.content[:3000]}",
                ]
            )
        )
    return (
        "다음 기사에서 기업의 사업 방향, 기술, 성장 전망, 조직 문화를 보여 주는 "
        "키워드를 최대 10개 추출하세요. 중요도는 0~10, 정서는 긍정이면 true, "
        "부정이면 false로 표시하세요.\n\n" + "\n\n".join(sections)
    )
