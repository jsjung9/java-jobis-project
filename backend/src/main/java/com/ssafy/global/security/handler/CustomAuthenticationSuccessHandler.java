package com.ssafy.global.security.handler;

import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.service.RefreshTokenService;
import com.ssafy.global.security.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomUsernamePasswordAuthenticationFilter 에서 인증 성공 후 실행되는 핸들러
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증 성공 시 동작 정의

        // 토큰 생성
        JwtDto jwtDto = jwtUtil.generateJwtDto(authentication);

        // redis에 refreshToken 저장
        refreshTokenService.addRefreshToken(Integer.valueOf(authentication.getName()), jwtDto.getRefreshToken());

        // 응답객체 타입 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 헤더에 토큰 담아 전송
        jwtUtil.setAccessAndRefreshToken(response, jwtDto);
    }

}
