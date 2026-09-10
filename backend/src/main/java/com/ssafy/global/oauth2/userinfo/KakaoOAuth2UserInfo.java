package com.ssafy.global.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Object id = attributes.get("id");
        return id == null ? null : String.valueOf(id);
    }

    @Override
    public String getEmail() {
        return getString(kakaoAccount(), "email");
    }

    @Override
    public String getNickname() {
        return getString(profile(), "nickname");
    }

    @Override
    public String getImageUrl() {
        return getString(profile(), "thumbnail_image_url");
    }

    private Map<?, ?> kakaoAccount() {
        Object value = attributes.get("kakao_account");
        return value instanceof Map<?, ?> map ? map : Map.of();
    }

    private Map<?, ?> profile() {
        Object value = kakaoAccount().get("profile");
        return value instanceof Map<?, ?> map ? map : Map.of();
    }

    private String getString(Map<?, ?> source, String key) {
        Object value = source.get(key);
        return value instanceof String stringValue ? stringValue : null;
    }
}
