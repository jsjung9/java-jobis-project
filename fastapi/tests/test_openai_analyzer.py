import json
from types import SimpleNamespace

from models import NewsArticle
from packages.openai_analyzer import analyze_news


class FakeResponses:
    def __init__(self) -> None:
        self.arguments = None

    def create(self, **kwargs):
        self.arguments = kwargs
        return SimpleNamespace(
            output_text=json.dumps(
                {
                    "keywords": [
                        {
                            "keyword": "반도체 소재",
                            "importance": 8,
                            "sentiment": True,
                        }
                    ]
                }
            )
        )


def test_uses_structured_non_persistent_response() -> None:
    responses = FakeResponses()
    client = SimpleNamespace(responses=responses)
    article = NewsArticle(
        title="테스트 기업의 신규 사업",
        content="기사 본문",
        date="2026-09-10",
        link="https://n.news.naver.com/article/001/1",
    )

    analysis = analyze_news([article], client=client, model="test-model")

    assert analysis.keywords[0].keyword == "반도체 소재"
    assert responses.arguments["model"] == "test-model"
    assert responses.arguments["store"] is False
    assert responses.arguments["text"]["format"]["type"] == "json_schema"


def test_skips_model_call_when_there_are_no_articles() -> None:
    assert analyze_news([]).keywords == []
