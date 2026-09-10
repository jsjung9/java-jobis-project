package com.ssafy.domain.patent.service;

import com.ssafy.domain.patent.dto.PatentDtoMapper;
import com.ssafy.domain.patent.dto.response.PatentResponse;
import com.ssafy.domain.patent.entity.Patent;
import com.ssafy.domain.patent.repository.PatentRepository;

import com.ssafy.global.response.code.ErrorCode;
import com.ssafy.global.response.exception.RestApiException;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatentService {

    private final PatentRepository patentRepository;

    public List<PatentResponse> findPatents(Integer companyId) {
        List<Patent> patents = patentRepository.findPatentsByCompanyId(companyId);

        if(patents.isEmpty()) {
            throw new RestApiException(ErrorCode.PATENT_NOT_FOUND);
        }

        return PatentDtoMapper.patentEntityToDtoList(patents);
    }

}
