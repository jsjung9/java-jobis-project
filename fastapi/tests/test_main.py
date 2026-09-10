from fastapi.testclient import TestClient

import main
from models import KeywordInsight, NewsAnalysis, NewsArticle

client = TestClient(main.app)


def test_health_endpoint() -> None:
    response = client.get("/health")

    assert response.status_code == 200
    assert response.json() == {"status": "ok"}


def test_news_endpoint_preserves_backend_contract(monkeypatch) -> None:
    article = NewsArticle(
        title="테스트 기업의 신규 사업",
        content="기사 본문",
        date=None,
        link="https://n.news.naver.com/article/001/1",
    )
    monkeypatch.setattr(main, "perform_news_search", lambda name: [article])
    monkeypatch.setattr(
        main,
        "analyze_news",
        lambda articles: NewsAnalysis(
            keywords=[KeywordInsight(keyword="신규 사업", importance=7, sentiment=True)]
        ),
    )

    response = client.get("/news/(주)%20테스트")

    assert response.status_code == 200
    assert response.json()["news"][0]["link"].startswith("https://n.news.naver.com/")
    assert response.json()["keywords"][0]["importance"] == 7


def test_rejects_name_removed_entirely_by_normalization() -> None:
    response = client.get("/news/(주)")

    assert response.status_code == 422
