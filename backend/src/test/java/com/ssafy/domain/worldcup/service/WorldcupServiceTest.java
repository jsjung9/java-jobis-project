package com.ssafy.domain.worldcup.service;

import com.ssafy.domain.worldcup.repository.WorldcupRepository;
import com.ssafy.global.response.exception.RestApiException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorldcupServiceTest {

    @Test
    void reportsMissingTournamentData() {
        WorldcupRepository repository = mock(WorldcupRepository.class);
        when(repository.findRandomWorldcups()).thenReturn(List.of());

        assertThatThrownBy(() -> new WorldcupService(repository).findRandomWorldcups())
                .isInstanceOf(RestApiException.class);
    }
}
