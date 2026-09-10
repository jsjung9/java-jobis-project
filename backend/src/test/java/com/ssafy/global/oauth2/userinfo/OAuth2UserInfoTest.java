package com.ssafy.global.oauth2.userinfo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OAuth2UserInfoTest {

    @Test
    void readsNestedKakaoAccountAndProfile() {
        KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(Map.of(
                "id", 12345L,
                "kakao_account", Map.of(
                        "email", "user@example.com",
                        "profile", Map.of(
                                "nickname", "준수",
                                "thumbnail_image_url", "https://example.com/profile.png"
                        )
                )
        ));

        assertThat(userInfo.getId()).isEqualTo("12345");
        assertThat(userInfo.getEmail()).isEqualTo("user@example.com");
        assertThat(userInfo.getNickname()).isEqualTo("준수");
        assertThat(userInfo.getImageUrl()).isEqualTo("https://example.com/profile.png");
    }

    @Test
    void returnsNullForMalformedProviderPayloads() {
        assertThat(new NaverOAuth2UserInfo(Map.of()).getEmail()).isNull();
        assertThat(new KakaoOAuth2UserInfo(Map.of()).getEmail()).isNull();
        assertThat(new KakaoOAuth2UserInfo(Map.of()).getId()).isNull();
    }
}
