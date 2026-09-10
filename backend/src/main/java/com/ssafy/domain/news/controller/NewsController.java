package com.ssafy.domain.news.controller;

import com.ssafy.domain.news.dto.response.NewsResponse;
import com.ssafy.domain.news.service.NewsService;
import com.ssafy.global.response.code.SuccessCode;
import com.ssafy.global.response.structure.SuccessResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Validated
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;

    @GetMapping("/{companyId}/{name}")
    public ResponseEntity<Object> getNews(
            @PathVariable("companyId") @Positive Long companyId,
            @PathVariable("name") @NotBlank String name
    ) {
        logger.info("REQUEST: news list: {}", name);

        NewsResponse newsResponseList = newsService.findNews(companyId.toString(), name);

        return SuccessResponse.createSuccess(SuccessCode.FIND_NEWS_SUCCESS, newsResponseList);
    }

}
