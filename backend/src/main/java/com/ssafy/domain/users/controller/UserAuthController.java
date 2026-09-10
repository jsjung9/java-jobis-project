package com.ssafy.domain.users.controller;

import com.ssafy.domain.users.dto.request.RegisterRequest;
import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.service.UserAuthService;
import com.ssafy.domain.users.service.UserService;
import com.ssafy.global.oauth2.util.RedirectUriStorage;
import com.ssafy.global.response.code.SuccessCode;
import com.ssafy.global.response.structure.SuccessResponse;
import com.ssafy.global.security.util.JwtUtil;
import com.ssafy.global.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Tag(name = "회원가입, 로그인 관리")
public class UserAuthController {

    private static final Set<String> SOCIAL_PROVIDERS = Set.of("google", "naver", "kakao");

    private final UserService userService;
    private final UserAuthService userAuthService;
    private final RedirectUriStorage redirectUriStorage;
    private final JwtUtil jwtUtil;
    private final AuthUtil authUtil;

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<Object> register(
            @Valid @RequestBody RegisterRequest request) {
        log.info("register 메서드 호출");
        userService.register(request);
        return SuccessResponse.createSuccess(SuccessCode.JOIN_SUCCESS);
    }

    @GetMapping("/login/{socialType}")
    @Operation(summary = "소셜로그인")
    public void socialRegister(
            @PathVariable String socialType,
            @RequestParam(name = "redirect_uri") String redirect_uri,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        if (!SOCIAL_PROVIDERS.contains(socialType)) {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다.");
        }
        redirectUriStorage.save(request, redirect_uri);
        response.sendRedirect("/api/oauth2/authorization/"+socialType);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        userAuthService.logout(authUtil.getLoginUserId());
        jwtUtil.clearRefreshTokenCookie(response);
        return SuccessResponse.createSuccess(SuccessCode.LOGOUT_SUCCESS);
    }

    @PostMapping("/token")
    @Operation(summary = "액세스 토큰 갱신")
    public ResponseEntity<Object> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.extractRefreshToken(request);
        JwtDto tokens = userAuthService.refresh(refreshToken);
        jwtUtil.setAccessAndRefreshToken(response, tokens);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    @Operation(summary = "이메일 중복조회")
    public ResponseEntity<Object> check(
            @RequestParam String email
    ) {
        final boolean check = userAuthService.checkEmail(email);
        if(check){
            return SuccessResponse.createSuccess(SuccessCode.CHECK_EMAIL_GOOD, true);
        } else {
            return SuccessResponse.createSuccess(SuccessCode.CHECK_EMAIL_BAD, false);
        }
    }
}
