package com.ssafy.global.security.util;

import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.exception.JwtInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.access-expired}")
    private Long accessTokenExpired;
    @Value("${jwt.refresh-expired}")
    private Long refreshTokenExpired;


    private final SecretKey SECRET_KEY;
    private final boolean refreshCookieSecure;
    private final String refreshCookieSameSite;
    private final String refreshCookiePath;

    private final static String BEARER_TYPE = "Bearer";
    private final static String BEARER_TYPE_PREFIX = "Bearer ";
    private static final int JWT_PREFIX_LENGTH = 7;


    private final static String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final static String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final static String CLAIM_USER_ID_KEY = "userId";

    private final static String ACCESS_TOKEN_HEADER = "Authorization";
    private final static String REFRESH_TOKEN_COOKIE = "refresh_token";


    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.cookie.secure:false}") boolean refreshCookieSecure,
            @Value("${jwt.cookie.same-site:Lax}") String refreshCookieSameSite,
            @Value("${server.servlet.context-path:/}") String contextPath
    ) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
        this.refreshCookieSecure = refreshCookieSecure;
        this.refreshCookieSameSite = refreshCookieSameSite;
        this.refreshCookiePath = contextPath == null || contextPath.isBlank() ? "/" : contextPath;
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
        setRefreshTokenCookie(response, jwtDto.getRefreshToken());
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite(refreshCookieSameSite)
                .path(refreshCookiePath)
                .maxAge(Duration.ofMillis(refreshTokenExpired))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite(refreshCookieSameSite)
                .path(refreshCookiePath)
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new JwtInvalidException();
        }
        for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new JwtInvalidException();
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
            return parseClaims(accessToken).get(CLAIM_USER_ID_KEY, String.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtInvalidException();
        }
    }

    /**
     * 토큰 검증 메서드
     */
    public boolean validateAccessToken(String token) {
        return validateToken(token, ACCESS_TOKEN_SUBJECT);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, REFRESH_TOKEN_SUBJECT);
    }

    private boolean validateToken(String token, String expectedSubject) {
        try {
            Claims claims = parseClaims(token);
            if (!expectedSubject.equals(claims.getSubject())) {
                throw new JwtInvalidException();
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtInvalidException();
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
