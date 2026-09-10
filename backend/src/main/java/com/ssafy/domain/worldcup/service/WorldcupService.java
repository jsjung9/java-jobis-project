package com.ssafy.domain.worldcup.service;

import com.ssafy.domain.worldcup.dto.WorldcupDtoMapper;
import com.ssafy.domain.worldcup.dto.response.WorldcupResponse;
import com.ssafy.domain.worldcup.entity.Worldcup;
import com.ssafy.domain.worldcup.repository.WorldcupRepository;
import com.ssafy.global.response.code.ErrorCode;
import com.ssafy.global.response.exception.RestApiException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorldcupService {

    private final WorldcupRepository worldcupRepository;

    public List<WorldcupResponse> findRandomWorldcups() {
        List<Worldcup> randomWorldcups = worldcupRepository.findRandomWorldcups();
        if (randomWorldcups.size() != 16) {
            throw new RestApiException(ErrorCode.WORLDCUP_NOT_FOUND);
        }

        return WorldcupDtoMapper.worldcupEntityListToDtoList(randomWorldcups);
    }

}
