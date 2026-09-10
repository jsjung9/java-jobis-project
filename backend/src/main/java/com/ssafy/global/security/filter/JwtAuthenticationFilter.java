package com.ssafy.global.security.filter;

import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.exception.UserNotFoundException;
import com.ssafy.domain.users.repository.UserRepository;
import com.ssafy.domain.users.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !StringUtils.hasText(request.getHeader(AUTHORIZATION_HEADER))
                && !StringUtils.hasText(request.getHeader("Refresh-Token"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String refreshToken = jwtUtil.extractRefreshToken(request)
                .filter(jwtUtil::validateToken)
                .orElse(null);

        // 리프레시 토큰을 헤더에 담아서 요청했다면 access token 연장 신청을 위함
        // redis의 리프레시 토큰과 일치하는지 판단 후 access token 재발급
        if(refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(request, response, refreshToken, filterChain);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    /**
     *  리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드
     *  파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있다면
     *  JwtService.createAccessToken()으로 AccessToken 생성,
     *  reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
     *  그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
     */








    public void checkRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.extractAccessToken(request);

        if (jwtUtil.validateToken(accessToken)) {
            String id = jwtUtil.extractId(accessToken);
            String myRefreshToken = jwtUtil.getRedisRefreshToken(id);

            if (refreshToken.equals(myRefreshToken)) {
                JwtDto reIssueJwt = jwtUtil.generateJwtDto(id);
                refreshTokenService.addRefreshToken(Integer.valueOf(id), reIssueJwt.getRefreshToken());

//                Users user = userRepository.findById(Integer.parseInt(id))
//                        .orElseThrow(UserNotFoundException::new);
                jwtUtil.setAccessAndRefreshToken(response, reIssueJwt);

            }
            filterChain.doFilter(request, response);
        }
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.extractAccessToken(request);
        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
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
