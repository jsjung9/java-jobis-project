package com.ssafy.domain.recommendation.repository;

import com.ssafy.domain.company.entity.Company;
import com.ssafy.domain.recommendation.entity.Recommendation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {

    @Query("select r.company from Recommendation r where r.worldcup.id = :id order by r.id")
    List<Company> findCompanyByWorldcupId(Long id);

}
