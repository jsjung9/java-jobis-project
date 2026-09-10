package com.ssafy.domain.recommendation.service;

import com.ssafy.domain.recommendation.repository.RecommendationRepository;
import com.ssafy.global.response.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock RecommendationRepository recommendationRepository;

    @Test
    void reportsMissingRecommendationInsteadOfReturningAmbiguousEmptyResult() {
        when(recommendationRepository.findCompanyByWorldcupId(10L)).thenReturn(List.of());
        RecommendationService service = new RecommendationService(recommendationRepository);

        assertThatThrownBy(() -> service.findCompanyByWorldcupId(10L))
                .isInstanceOf(RestApiException.class);
    }
}
