import logging
import os

from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException, Path
from fastapi.concurrency import run_in_threadpool
from fastapi.middleware.cors import CORSMiddleware

from models import NewsResult
from packages.naver_scraper import NewsSearchError, normalize_company_name, perform_news_search
from packages.openai_analyzer import AnalysisError, analyze_news

load_dotenv()

logger = logging.getLogger(__name__)


def allowed_origins() -> list[str]:
    value = os.getenv("CORS_ALLOWED_ORIGINS", "http://localhost:3000")
    return [origin.strip() for origin in value.split(",") if origin.strip()]


app = FastAPI(
    title="Jobis News Analysis API",
    version="1.0.0",
    description="기업 뉴스를 수집하고 구직 관점의 핵심 키워드를 추출합니다.",
)
app.add_middleware(
    CORSMiddleware,
    allow_origins=allowed_origins(),
    allow_credentials=True,
    allow_methods=["GET"],
    allow_headers=["Accept", "Authorization", "Content-Type"],
)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.get("/news/{name}", response_model=NewsResult)
async def company_news(
    name: str = Path(min_length=1, max_length=80),
) -> NewsResult:
    normalized_name = normalize_company_name(name)
    if not normalized_name:
        raise HTTPException(status_code=422, detail="유효한 기업명을 입력해 주세요.")

    try:
        articles = await run_in_threadpool(perform_news_search, normalized_name)
        analysis = await run_in_threadpool(analyze_news, articles)
    except (NewsSearchError, AnalysisError):
        logger.exception("Company news analysis failed")
        raise HTTPException(
            status_code=502,
            detail="외부 뉴스 분석 서비스에서 응답을 가져오지 못했습니다.",
        ) from None

    return NewsResult(news=articles, keywords=analysis.keywords)
