package com.ssafy.domain.users.repository;

import com.ssafy.domain.users.entity.SocialType;
import com.ssafy.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    Optional<Users> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
