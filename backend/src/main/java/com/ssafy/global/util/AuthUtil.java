package com.ssafy.global.util;

import com.ssafy.domain.users.entity.Users;
import com.ssafy.domain.users.exception.UserNotFoundException;
import com.ssafy.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;

    public Integer getLoginUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Integer.valueOf(userId);
    }

    public Users getLoginUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(Integer.valueOf(userId))
                .orElseThrow(UserNotFoundException::new);
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }

}
