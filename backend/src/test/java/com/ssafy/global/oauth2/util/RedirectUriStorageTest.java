package com.ssafy.global.oauth2.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class RedirectUriStorageTest {

    private final RedirectUriStorage storage = new RedirectUriStorage(
        List.of("http://localhost:3000", "https://jobis.example.com")
    );

    @Test
    void acceptsConfiguredOriginAndDropsPathAndQuery() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        storage.save(request, "https://jobis.example.com/login?next=/company");

        assertThat(storage.consume(request)).isEqualTo("https://jobis.example.com");
        assertThat(storage.consume(request)).isEqualTo("http://localhost:3000");
    }

    @Test
    void rejectsUnconfiguredOrNonHttpRedirects() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> storage.save(request, "https://attacker.example"))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> storage.save(request, "javascript:alert(1)"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void keepsConcurrentBrowserSessionsIndependent() {
        MockHttpServletRequest first = new MockHttpServletRequest();
        MockHttpServletRequest second = new MockHttpServletRequest();

        storage.save(first, "http://localhost:3000");
        storage.save(second, "https://jobis.example.com");

        assertThat(storage.consume(first)).isEqualTo("http://localhost:3000");
        assertThat(storage.consume(second)).isEqualTo("https://jobis.example.com");
    }
}
