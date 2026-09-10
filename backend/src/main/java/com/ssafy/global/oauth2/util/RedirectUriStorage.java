package com.ssafy.global.oauth2.util;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class RedirectUriStorage {

    private static final String SESSION_ATTRIBUTE = "oauth2_redirect_origin";

    private final List<String> authorizedOrigins;

    public RedirectUriStorage(
        @Value("${oauth2.authorized-redirect-origins:http://localhost:3000}") List<String> authorizedOrigins
    ) {
        this.authorizedOrigins = authorizedOrigins.stream().map(this::normalizeOrigin).toList();
    }

    public void save(HttpServletRequest request, String candidate) {
        String origin = normalizeOrigin(candidate);
        if (!authorizedOrigins.contains(origin)) {
            throw new IllegalArgumentException("허용되지 않은 로그인 리다이렉트 주소입니다.");
        }
        request.getSession(true).setAttribute(SESSION_ATTRIBUTE, origin);
    }

    public String consume(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return authorizedOrigins.get(0);
        }
        Object value = session.getAttribute(SESSION_ATTRIBUTE);
        session.removeAttribute(SESSION_ATTRIBUTE);
        return value instanceof String redirectOrigin ? redirectOrigin : authorizedOrigins.get(0);
    }

    private String normalizeOrigin(String candidate) {
        URI uri = URI.create(candidate);
        if (uri.getScheme() == null || uri.getHost() == null
            || !(uri.getScheme().equals("http") || uri.getScheme().equals("https"))) {
            throw new IllegalArgumentException("올바른 HTTP(S) 주소가 아닙니다.");
        }
        int port = uri.getPort();
        return uri.getScheme() + "://" + uri.getHost() + (port < 0 ? "" : ":" + port);
    }
}
