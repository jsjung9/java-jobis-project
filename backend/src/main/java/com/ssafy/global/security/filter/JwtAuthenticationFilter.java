package com.ssafy.global.security.filter;

import com.ssafy.domain.users.exception.UserNotFoundException;
import com.ssafy.domain.users.repository.UserRepository;
import com.ssafy.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !StringUtils.hasText(request.getHeader(AUTHORIZATION_HEADER));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.extractAccessToken(request);
        if (jwtUtil.validateAccessToken(accessToken)) {
            String userId = jwtUtil.extractId(accessToken);

            // UserDetails를 직접 구현한 Users 엔티티를 사용하여 Authentication 객체를 생성
            UserDetails userDetails = userRepository.findById(Integer.parseInt(userId))
                    .orElseThrow(UserNotFoundException::new);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, null);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
