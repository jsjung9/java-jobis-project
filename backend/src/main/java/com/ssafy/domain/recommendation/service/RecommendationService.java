package com.ssafy.domain.recommendation.service;

import com.ssafy.domain.company.dto.response.CompanyResponse;
import com.ssafy.domain.company.entity.Company;
import com.ssafy.domain.recommendation.repository.RecommendationRepository;
import com.ssafy.global.response.code.ErrorCode;
import com.ssafy.global.response.exception.RestApiException;
import static com.ssafy.domain.company.dto.CompanyDtoMapper.companyEntityToDtoList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public List<CompanyResponse> findCompanyByWorldcupId(Long id) {
        List<Company> getCompanyList = recommendationRepository.findCompanyByWorldcupId(id);
        if (getCompanyList.isEmpty()) {
            throw new RestApiException(ErrorCode.RECOMMENDATION_NOT_FOUND);
        }
        return companyEntityToDtoList(getCompanyList);
    }

}
