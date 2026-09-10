package com.ssafy.domain.news.service;

import com.ssafy.domain.news.dto.NewsData;
import com.ssafy.domain.news.dto.response.NewsResponse;
import com.ssafy.domain.news.entity.NewsRedis;
import com.ssafy.domain.news.repository.NewsRedisRepository;
import com.ssafy.global.response.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock NewsRedisRepository newsRedisRepository;
    @Mock NewsApiClient newsApiClient;

    @InjectMocks
    NewsService newsService;

    @Test
    void returnsCachedNewsWithoutCallingExternalService() {
        NewsRedis cached = NewsRedis.builder()
                .id("15")
                .news(List.of(new NewsRedis.Article("title", "content", "2026-09-10", "https://example.com")))
                .keywords(List.of(new NewsRedis.Keyword("채용", 3, true)))
                .build();
        when(newsRedisRepository.findById("15")).thenReturn(Optional.of(cached));

        NewsResponse response = newsService.findNews("15", "기업명");

        assertThat(response.getCompanyId()).isEqualTo("15");
        assertThat(response.getNews()).hasSize(1);
        verify(newsApiClient, never()).fetch("기업명");
    }

    @Test
    void cachesCompleteExternalResponse() {
        when(newsRedisRepository.findById("20")).thenReturn(Optional.empty());
        NewsData external = new NewsData();
        external.setNews(List.of(new NewsData.Article(
                "title", "content", "2026-09-10", "https://example.com")));
        external.setKeywords(List.of(new NewsData.Keyword("기술", 5, false)));
        when(newsApiClient.fetch("테스트기업")).thenReturn(external);

        NewsResponse response = newsService.findNews("20", "테스트기업");

        assertThat(response.getNews()).extracting(NewsResponse.Article::getTitle).containsExactly("title");
        verify(newsRedisRepository).save(org.mockito.ArgumentMatchers.any(NewsRedis.class));
    }

    @Test
    void rejectsExternalResponseWithoutArticles() {
        when(newsRedisRepository.findById("30")).thenReturn(Optional.empty());
        NewsData external = new NewsData();
        external.setNews(List.of());
        external.setKeywords(List.of());
        when(newsApiClient.fetch("빈기업")).thenReturn(external);

        assertThatThrownBy(() -> newsService.findNews("30", "빈기업"))
                .isInstanceOf(RestApiException.class);
    }
}
