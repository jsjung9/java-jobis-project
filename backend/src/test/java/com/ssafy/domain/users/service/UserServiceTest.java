package com.ssafy.domain.users.service;

import com.ssafy.domain.company.repository.CompanyScrapRepository;
import com.ssafy.domain.users.dto.request.RegisterRequest;
import com.ssafy.domain.users.entity.Users;
import com.ssafy.domain.users.exception.DuplicatedEmailException;
import com.ssafy.domain.users.repository.TendencyRepository;
import com.ssafy.domain.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TendencyRepository tendencyRepository;
    @Mock CompanyScrapRepository companyScrapRepository;

    @InjectMocks
    UserService userService;

    @Test
    void normalizesEmailAndStoresOnlyEncodedPassword() {
        when(passwordEncoder.encode("safe-password")).thenReturn("encoded");
        when(userRepository.saveAndFlush(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(new RegisterRequest("  MEMBER@Example.COM ", "safe-password"));

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).saveAndFlush(userCaptor.capture());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("member@example.com");
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded");
    }

    @Test
    void translatesDatabaseEmailRaceIntoDomainConflict() {
        when(passwordEncoder.encode("safe-password")).thenReturn("encoded");
        when(userRepository.saveAndFlush(any(Users.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> userService.register(
                new RegisterRequest("member@example.com", "safe-password")))
                .isInstanceOf(DuplicatedEmailException.class);
    }
}
