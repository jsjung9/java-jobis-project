package com.ssafy.domain.users.service;

import com.ssafy.domain.users.dto.JwtDto;
import com.ssafy.domain.users.entity.SocialType;
import com.ssafy.domain.users.entity.Users;
import com.ssafy.domain.users.exception.JwtInvalidException;
import com.ssafy.domain.users.exception.UserNotFoundException;
import com.ssafy.domain.users.repository.UserRepository;
import com.ssafy.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public boolean checkEmail(String email) {
        return !userRepository.existsByEmail(email.trim().toLowerCase(Locale.ROOT));
    }

    public void logout(Integer userId) {
        refreshTokenService.deleteRefreshTokenByUserId(userId.toString());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    public JwtDto refresh(String refreshToken) {
        jwtUtil.validateRefreshToken(refreshToken);
        String userId = jwtUtil.extractId(refreshToken);
        String storedRefreshToken = refreshTokenService.findRefreshToken(userId);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new JwtInvalidException();
        }
        userRepository.findById(Integer.valueOf(userId))
                .orElseThrow(UserNotFoundException::new);

        JwtDto tokens = jwtUtil.generateJwtDto(userId);
        refreshTokenService.addRefreshToken(Integer.valueOf(userId), tokens.getRefreshToken());
        return tokens;
    }

    /**
     * 소셜타입과 소셜 id로 유저 찾기
     * 이미 가입했던 유저와 첫 회원가입하는 유저를 판별하기위해 작성
     * 예외를 던지지 않는 이유는 없을 때 회원가입 시키기 위함
     */
    public Users findSocialUser(SocialType socialType, String socialId) {
        return userRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .orElse(null);
    }

}
