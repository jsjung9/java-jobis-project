package com.ssafy.domain.news.service;

import com.ssafy.domain.news.dto.NewsData;
import com.ssafy.domain.news.dto.NewsDtoMapper;
import com.ssafy.domain.news.dto.response.NewsResponse;
import com.ssafy.domain.news.entity.NewsRedis;
import com.ssafy.domain.news.repository.NewsRedisRepository;
import com.ssafy.global.response.code.ErrorCode;
import com.ssafy.global.response.exception.RestApiException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRedisRepository newsRedisRepository;
    private final NewsApiClient newsApiClient;

    public NewsResponse findNews(String companyId, String name) {
        Optional<NewsRedis> newsRedis = newsRedisRepository.findById(companyId);

        if (newsRedis.isEmpty()) {
            log.info("{} ({}) 기사를 Redis에서 찾을 수 없습니다. 외부 API 호출 중...", name, companyId);

            NewsData externalNews = newsApiClient.fetch(name);

            if (hasContent(externalNews)) {
                log.info("{} ({}) 기사를 Redis에 저장합니다...", name, companyId);

                return saveNewsToRedisAndReturnResponse(companyId, externalNews);
            } else {
                log.info("{} ({}) 기사를 외부 API에서도 찾을 수 없습니다.", name, companyId);

                throw new RestApiException(ErrorCode.NEWS_NOT_FOUND);
            }
        } else {
            return NewsDtoMapper.newsRedisToDto(newsRedis.get());
        }
    }

    private NewsResponse saveNewsToRedisAndReturnResponse(String companyId, NewsData newsData) {

        NewsRedis newsRedis = convertToNewsRedis(companyId, newsData);

        newsRedisRepository.save(newsRedis);

        return NewsDtoMapper.newsRedisToDto(newsRedis);
    }

    private NewsRedis convertToNewsRedis(String companyId, NewsData newsData) {
        List<NewsRedis.Article> articles = newsData.getNews().stream()
                .map(article -> new NewsRedis.Article(article.getTitle(), article.getContent(), article.getDate(), article.getLink()))
                .toList();

        List<NewsRedis.Keyword> keywords = newsData.getKeywords().stream()
                .map(keyword -> new NewsRedis.Keyword(keyword.getKeyword(), keyword.getImportance(), keyword.isSentiment()))
                .toList();

        return NewsRedis.builder()
                .id(companyId)
                .news(articles)
                .keywords(keywords)
                .build();
    }

    private boolean hasContent(NewsData newsData) {
        return newsData.getNews() != null
                && !newsData.getNews().isEmpty()
                && newsData.getKeywords() != null;
    }

}
