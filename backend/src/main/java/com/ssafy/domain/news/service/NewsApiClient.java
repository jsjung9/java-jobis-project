package com.ssafy.domain.news.service;

import com.ssafy.domain.news.dto.NewsData;
import com.ssafy.global.response.code.ErrorCode;
import com.ssafy.global.response.exception.RestApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;

@Component
public class NewsApiClient {

    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(5);

    private final WebClient webClient;

    public NewsApiClient(
            WebClient.Builder webClientBuilder,
            @Value("${services.news-base-url}") String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public NewsData fetch(String companyName) {
        try {
            NewsData response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/news/{name}").build(companyName))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.createException())
                    .bodyToMono(NewsData.class)
                    .block(RESPONSE_TIMEOUT);

            if (response == null) {
                throw new RestApiException(ErrorCode.NEWS_NOT_FOUND);
            }
            return response;
        } catch (RestApiException exception) {
            throw exception;
        } catch (WebClientException | IllegalStateException exception) {
            throw new RestApiException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
        }
    }
}
