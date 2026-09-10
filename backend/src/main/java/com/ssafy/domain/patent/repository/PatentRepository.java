package com.ssafy.domain.patent.repository;

import com.ssafy.domain.patent.entity.Patent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatentRepository extends JpaRepository<Patent, Long> {

    List<Patent> findPatentsByCompanyId(Integer companyId);

}
