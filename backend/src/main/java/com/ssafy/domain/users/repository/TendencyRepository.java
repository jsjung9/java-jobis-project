package com.ssafy.domain.users.repository;

import com.ssafy.domain.users.entity.Tendency;
import com.ssafy.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TendencyRepository extends JpaRepository<Tendency, Integer> {

    // 회원성향 조회
    List<Tendency> findAllByUsers(Users users);
}
