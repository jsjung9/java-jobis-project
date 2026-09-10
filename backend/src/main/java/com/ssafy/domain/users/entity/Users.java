package com.ssafy.domain.users.entity;

import com.ssafy.domain.users.dto.UserDto;
import com.ssafy.global.oauth2.userinfo.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * UserDetails 인터페이스를 구현하여
 * 엔티티객체 자체를 인증객체로 활용
 * 
 */
@Entity
@Table(name = "users")
@Getter
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id")
    private String socialId;


    public static Users createUser(String email, String encodedPassword) {
        Users user = new Users();
        user.email = email;
        user.password = encodedPassword;
        return user;
    }

    public static Users createSocialUser(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        Users user = new Users();
        user.email = oauth2UserInfo.getEmail();
        user.socialType = socialType;
        user.socialId = oauth2UserInfo.getId();
        return user;
    }

    public UserDto toUserDto(Users users) {
        return UserDto.builder()
                .id(users.getId())
                .email(users.getEmail())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    // 여기서의 userName은 유저 이름이 아닌 UserDetails 객체의 식별자를 나타냄 - user pk로 사용
    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {  // 계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {  // 계정 잠금 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 자격 증명 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() {  // 계정 활성화 여부
        return true;
    }
}
