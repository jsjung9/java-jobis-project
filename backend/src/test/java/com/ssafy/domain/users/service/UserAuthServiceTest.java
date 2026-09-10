package com.ssafy.domain.users.service;

import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.entity.Users;
import com.ssafy.domain.users.exception.JwtInvalidException;
import com.ssafy.domain.users.repository.UserRepository;
import com.ssafy.global.security.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock JwtUtil jwtUtil;
    @Mock RefreshTokenService refreshTokenService;

    @InjectMocks
    UserAuthService userAuthService;

    @Test
    void rotatesMatchingRefreshToken() {
        JwtDto rotated = new JwtDto("Bearer", "new-access", "new-refresh");
        when(jwtUtil.extractId("old-refresh")).thenReturn("7");
        when(refreshTokenService.findRefreshToken("7")).thenReturn("old-refresh");
        when(userRepository.findById(7)).thenReturn(Optional.of(mock(Users.class)));
        when(jwtUtil.generateJwtDto("7")).thenReturn(rotated);

        JwtDto result = userAuthService.refresh("old-refresh");

        assertThat(result).isSameAs(rotated);
        verify(jwtUtil).validateRefreshToken("old-refresh");
        verify(refreshTokenService).addRefreshToken(7, "new-refresh");
    }

    @Test
    void rejectsRefreshTokenThatDoesNotMatchServerState() {
        when(jwtUtil.extractId("stolen-refresh")).thenReturn("7");
        when(refreshTokenService.findRefreshToken("7")).thenReturn("actual-refresh");

        assertThatThrownBy(() -> userAuthService.refresh("stolen-refresh"))
                .isInstanceOf(JwtInvalidException.class);
    }
}
