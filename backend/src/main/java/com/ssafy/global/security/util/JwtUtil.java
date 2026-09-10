package com.ssafy.global.security.util;

import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.exception.JwtInvalidException;
import com.ssafy.domain.users.repository.RefreshTokenRedisRepository;
import com.ssafy.domain.users.service.RefreshTokenService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtUtil {

    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.access-expired}")
    private Long accessTokenExpired;
    @Value("${jwt.refresh-expired}")
    private Long refreshTokenExpired;


    private final SecretKey SECRET_KEY;

    private final static String BEARER_TYPE = "Bearer";
    private final static String BEARER_TYPE_PREFIX = "Bearer ";
    private static final int JWT_PREFIX_LENGTH = 7;


    private final static String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final static String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final static String CLAIM_USER_ID_KEY = "userId";

    private final static String ACCESS_TOKEN_HEADER = "Authorization";
    private final static String REFRESH_TOKEN_HEADER = "Refresh-Token";


    public JwtUtil(@Autowired RefreshTokenService refreshTokenService, @Value("${jwt.secret}") String secret) {
        this.refreshTokenService = refreshTokenService;
        byte[] keyBytes = secret.getBytes();
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     *
     * accessToken, refreshToken을 생성하고
     * DTO에 해당 정보를 담아 리턴해주는 메서드
     *
     */
    public JwtDto generateJwtDto(Authentication authentication) {
        String accessToken = Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                // authentication에서의 name은 사용자를 고유하게 식별할 수 있는 값을 의미
                // user pk로 설정
                .claim(CLAIM_USER_ID_KEY, authentication.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpired))
                .signWith(SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .claim(CLAIM_USER_ID_KEY, authentication.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpired))
                .signWith(SECRET_KEY)
                .compact();

        return JwtDto.builder()
                .type(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public JwtDto generateJwtDto(String userId) {
        String accessToken = Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .claim(CLAIM_USER_ID_KEY, userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpired))
                .signWith(SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .claim(CLAIM_USER_ID_KEY, userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpired))
                .signWith(SECRET_KEY)
                .compact();

        return JwtDto.builder()
                .type(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void setAccessAndRefreshToken(HttpServletResponse response, JwtDto jwtDto) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(ACCESS_TOKEN_HEADER, jwtDto.getType()+" "+jwtDto.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, jwtDto.getRefreshToken());
    }

    /**
     * 헤더에서 RefreshToken 추출
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_HEADER));
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : < Bearer XXX >에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public String extractAccessToken(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_TOKEN_HEADER);
        if(token == null) throw new JwtInvalidException();
        
        if(!token.startsWith(BEARER_TYPE_PREFIX)) throw new JwtInvalidException();

        return token.substring(JWT_PREFIX_LENGTH);
    }

    /**
     * 헤더에서 userPK 추출
     */
    public String extractId(String accessToken) {
        try {
            return Jwts.parser().verifyWith(SECRET_KEY).build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .get(CLAIM_USER_ID_KEY, String.class);
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return null;
        }
    }

    /**
     * userPk로 redis에서 토큰 조회
     */
    public String getRedisRefreshToken(String userId) {
        return refreshTokenService.findRefreshToken(userId);
    }

    /**
     * 토큰 검증 메서드
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.error("유효하지 않은 토큰", e);
            throw new JwtInvalidException();
        }

    }
}
