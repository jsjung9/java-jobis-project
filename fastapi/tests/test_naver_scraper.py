from packages.naver_scraper import (
    REQUEST_TIMEOUT,
    calculate_similarity,
    normalize_company_name,
    perform_news_search,
)


class FakeResponse:
    def __init__(self, text: str):
        self.text = text

    def raise_for_status(self) -> None:
        return None


class FakeSession:
    def __init__(self, responses: list[FakeResponse]):
        self.responses = iter(responses)
        self.requests: list[dict] = []

    def get(self, url: str, **kwargs) -> FakeResponse:
        self.requests.append({"url": url, **kwargs})
        return next(self.responses)


def test_normalizes_common_company_suffixes() -> None:
    assert normalize_company_name("(주) 주식회사 테스트 Co. Ltd.") == "테스트"


def test_collects_trusted_naver_article_with_timeout() -> None:
    search_html = """
        <div class="info_group">
          <a class="info" href="https://example.com/press">언론사</a>
          <a class="info" href="https://n.news.naver.com/article/001/1">네이버뉴스</a>
        </div>
    """
    article_html = """
        <h2 class="media_end_head_headline">테스트 기업이 신규 사업을 시작했다</h2>
        <article id="dic_area">구체적인 기사 본문입니다.</article>
        <span class="media_end_head_info_datestamp_time"
              data-date-time="2026-09-10 09:00:00"></span>
    """
    session = FakeSession([FakeResponse(search_html), FakeResponse(article_html)])

    articles = perform_news_search(
        "테스트",
        last_page=1,
        max_articles=1,
        session=session,
    )

    assert len(articles) == 1
    assert articles[0].link.host == "n.news.naver.com"
    assert articles[0].content == "구체적인 기사 본문입니다."
    assert all(request["timeout"] == REQUEST_TIMEOUT for request in session.requests)


def test_does_not_fetch_untrusted_article_links() -> None:
    search_html = """
        <div class="info_group">
          <a class="info" href="https://attacker.example/internal">기사</a>
        </div>
    """
    session = FakeSession([FakeResponse(search_html)])

    assert perform_news_search("테스트", last_page=1, session=session) == []
    assert len(session.requests) == 1


def test_similarity_compares_unicode_text() -> None:
    assert calculate_similarity("동일한 한국어 기사", "동일한 한국어 기사") == 1.0
