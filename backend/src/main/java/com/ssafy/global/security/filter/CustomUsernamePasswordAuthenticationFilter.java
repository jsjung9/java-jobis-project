package com.ssafy.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.domain.users.dto.request.LoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;
import java.util.Locale;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/user/login";  // /auth/login으로 오는 요청을 처리
    private static final String HTTP_METHOD_POST = HttpMethod.POST.name();    //HTTP 메서드의 방식은 POST

    private static final RequestMatcher LOGIN_REQUEST_MATCHER =
            PathPatternRequestMatcher.withDefaults()
                    .matcher(HttpMethod.POST, DEFAULT_LOGIN_REQUEST_URL);

    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(LOGIN_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }


    /**
     * 사용자의 로그인 요청을 처리하는 필터.
     * HTTP 요청을 받아 적절한 요청인지 검증한 후
     * 유효한 사용자인지 확인.
     * 확인된 정보를 기반으로 인증 객체를 생성하여
     * AuthenticationManager에 전달.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return Authentication 객체
     * @throws AuthenticationException 인증에 실패한 경우 발생하는 예외
     * @throws IOException              입출력 오류가 발생한 경우 발생하는 예외
     * @throws ServletException         서블릿 오류가 발생한 경우 발생하는 예외
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 사용자 정보를 기반으로 인증 시도

        if (!request.getMethod().equals(HTTP_METHOD_POST)) {
            throw new AuthenticationServiceException("post 요청이 아닙니다");
        }

        String contentType = request.getContentType();
        if (contentType == null
                || !MediaType.APPLICATION_JSON.isCompatibleWith(MediaType.parseMediaType(contentType))) {
            throw new AuthenticationServiceException("json 타입 요청이 아닙니다");
        }

        // 요청으로부터 사용자 정보를 읽어와 RegisterRequest 객체로 변환
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        // 인증 요청 객체 생성 (사용자 이메일과 패스워드)
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail().trim().toLowerCase(Locale.ROOT),
                loginRequest.getPassword()
        );
        // 인증 성공 시 Authentication 객체 반환
        // authenticationManager는 usersDetailsService를 통해 userDetails 객체를 가져오고
        // 해당 userDetails가 사용 가능한 사용자인지 확인 후 최종 인증 성공, 실패 여부 판단
        return getAuthenticationManager().authenticate(authRequest);
    }


}
