package com.ssafy.global.security.util;

import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.exception.JwtInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(
                "test-jwt-secret-key-must-be-at-least-32-bytes-long",
                false,
                "Lax",
                "/api"
        );
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpired", 60_000L);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpired", 120_000L);
    }

    @Test
    void distinguishesAccessAndRefreshTokenPurposes() {
        JwtDto tokens = jwtUtil.generateJwtDto("42");

        assertThat(jwtUtil.validateAccessToken(tokens.getAccessToken())).isTrue();
        assertThat(jwtUtil.validateRefreshToken(tokens.getRefreshToken())).isTrue();
        assertThat(jwtUtil.extractId(tokens.getRefreshToken())).isEqualTo("42");
        assertThatThrownBy(() -> jwtUtil.validateRefreshToken(tokens.getAccessToken()))
                .isInstanceOf(JwtInvalidException.class);
    }

    @Test
    void writesRefreshTokenOnlyToProtectedCookie() {
        JwtDto tokens = jwtUtil.generateJwtDto("42");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtUtil.setAccessAndRefreshToken(response, tokens);

        assertThat(response.getHeader("Authorization")).startsWith("Bearer ");
        assertThat(response.getHeader("Set-Cookie"))
                .contains("refresh_token=")
                .contains("HttpOnly")
                .contains("SameSite=Lax")
                .contains("Path=/api");
        assertThat(response.getHeader("Refresh-Token")).isNull();
    }
}
