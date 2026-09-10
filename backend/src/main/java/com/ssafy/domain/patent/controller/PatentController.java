package com.ssafy.domain.patent.controller;

import com.ssafy.domain.patent.dto.response.PatentResponse;
import com.ssafy.domain.patent.service.PatentService;
import com.ssafy.global.response.code.SuccessCode;
import com.ssafy.global.response.structure.SuccessResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patent")
@Validated
public class PatentController {

    private static final Logger logger = LoggerFactory.getLogger(PatentController.class);

    private final PatentService patentService;

    @GetMapping("/{companyId}")
    public ResponseEntity<Object> patentList(@PathVariable("companyId") @Positive Integer companyId) {
        logger.info("REQUEST: PatentList list: {}", companyId);

        List<PatentResponse> patentResponseList = patentService.findPatents(companyId);

        return SuccessResponse.createSuccess(SuccessCode.FIND_PATENT_SUCCESS, patentResponseList);
    }

}
