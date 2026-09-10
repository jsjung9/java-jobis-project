package com.ssafy.domain.news.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

import lombok.Data;

@RedisHash(value = "NewsRedis", timeToLive = 3600)
@Data
@Builder
public class NewsRedis {

    @Id
    private String id;

    private List<Article> news;

    private List<Keyword> keywords;

    @Data
    @AllArgsConstructor
    public static class Article {
        private String title;
        private String content;
        private String date;
        private String link;
    }

    @Data
    @AllArgsConstructor
    public static class Keyword {
        private String keyword;
        private int importance;
        private boolean sentiment;
    }

}
