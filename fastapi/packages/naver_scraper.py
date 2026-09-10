import difflib
import os
import re
import time
from collections.abc import Callable
from urllib.parse import urlsplit

import requests
from bs4 import BeautifulSoup

from models import NewsArticle

SEARCH_URL = os.getenv("NAVER_NEWS_SEARCH_URL", "https://search.naver.com/search.naver")
ALLOWED_ARTICLE_HOSTS = frozenset({"n.news.naver.com", "news.naver.com"})
USER_AGENT = "Mozilla/5.0 (compatible; JobisPortfolio/1.0)"
REQUEST_TIMEOUT = (3.05, 5)


class NewsSearchError(RuntimeError):
    pass


def normalize_company_name(company_name: str) -> str:
    normalized = re.sub(
        r"\(.*?\)|주식회사|㈜|\(주\)|\s*Co\.?\s*Ltd\.?",
        "",
        company_name,
        flags=re.IGNORECASE,
    )
    return normalized.strip().strip('"').strip("'")


def perform_news_search(
    normal_keyword: str,
    last_page: int = 15,
    max_articles: int = 3,
    *,
    session: requests.Session | None = None,
    time_budget_seconds: float = 20,
    monotonic: Callable[[], float] = time.monotonic,
) -> list[NewsArticle]:
    keyword = normalize_company_name(normal_keyword)
    if not keyword:
        return []
    if last_page < 1 or max_articles < 1:
        raise ValueError("last_page와 max_articles는 1 이상이어야 합니다.")

    client = session or requests.Session()
    started_at = monotonic()
    articles: list[NewsArticle] = []

    try:
        for start in range(1, last_page * 10, 10):
            if monotonic() - started_at >= time_budget_seconds:
                break

            search_response = client.get(
                SEARCH_URL,
                params={
                    "where": "news",
                    "sm": "tab_jum",
                    "query": f'"{keyword}"',
                    "start": start,
                    "nso": "so:r,p:1y",
                    "sort": 1,
                },
                headers={"User-Agent": USER_AGENT},
                timeout=REQUEST_TIMEOUT,
            )
            search_response.raise_for_status()
            search_document = BeautifulSoup(search_response.text, "html.parser")

            if search_document.select_one(".api_noresult_wrap"):
                break

            for group in search_document.select("div.info_group"):
                article_link = _naver_article_link(group)
                if article_link is None:
                    continue

                article = _fetch_article(client, article_link)
                if article is None or keyword.casefold() not in article.title.casefold():
                    continue
                if _is_duplicate(articles, article):
                    continue

                articles.append(article)
                if len(articles) >= max_articles:
                    return articles
    except requests.RequestException as exc:
        raise NewsSearchError("뉴스 제공자 호출에 실패했습니다.") from exc
    finally:
        if session is None:
            client.close()

    return articles


def _naver_article_link(group: BeautifulSoup) -> str | None:
    for anchor in reversed(group.select("a.info[href]")):
        link = anchor.get("href")
        if not isinstance(link, str):
            continue
        parsed = urlsplit(link)
        if parsed.scheme == "https" and parsed.hostname in ALLOWED_ARTICLE_HOSTS:
            return link
    return None


def _fetch_article(client: requests.Session, link: str) -> NewsArticle | None:
    response = client.get(
        link,
        headers={"User-Agent": USER_AGENT},
        timeout=REQUEST_TIMEOUT,
    )
    response.raise_for_status()

    document = BeautifulSoup(response.text, "html.parser")
    title = document.select_one(".media_end_head_headline")
    content = document.select_one("#dic_area")
    date_element = document.select_one(".media_end_head_info_datestamp_time")
    if title is None or content is None:
        return None

    return NewsArticle(
        title=title.get_text(" ", strip=True),
        content=content.get_text(" ", strip=True),
        date=date_element.get("data-date-time") if date_element else None,
        link=link,
    )


def _is_duplicate(saved_articles: list[NewsArticle], candidate: NewsArticle) -> bool:
    return any(
        calculate_similarity(candidate.title, saved.title) > 0.7
        or calculate_similarity(candidate.content, saved.content) > 0.7
        for saved in saved_articles
    )


def calculate_similarity(first: str, second: str) -> float:
    return difflib.SequenceMatcher(None, first, second).ratio()
