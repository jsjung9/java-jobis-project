package com.ssafy.domain.company.repository;

import com.ssafy.domain.company.entity.Company;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CompanyRepository extends JpaRepository<Company, Integer> {

    // 상위기업 top10 조회
    List<Company> findByOrderByViewsDesc(Limit limit);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Company c set c.views = coalesce(c.views, 0) + 1 where c.id = :companyId")
    int incrementViews(@Param("companyId") Integer companyId);
}
