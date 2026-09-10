package com.ssafy.domain.company.controller;


import com.ssafy.domain.company.dto.response.*;
import com.ssafy.domain.company.service.CompanyService;
import com.ssafy.global.response.code.SuccessCode;
import com.ssafy.global.response.structure.SuccessResponse;
import com.ssafy.global.util.AuthUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
@Slf4j
@Tag(name = "기업API")
public class CompanyController {

    private final CompanyService companyService;
    private final AuthUtil authUtil;


    /**
     * 기업검색
     * @param pageable
     * @param keyword
     * @param location
     * @param type
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<Object> companySearch(
                                                @PageableDefault(size = 20) Pageable pageable,
//                                                @RequestParam(required = false) Integer id,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) List<String> location,
                                                @RequestParam(required = false) List<String> type) {
        Page<CompanySearchResponse> companySearchResponseList = companyService.getCompanies(pageable, keyword, location, type);
        return SuccessResponse.createSuccess(SuccessCode.SEARCH_COMPANY_SUCCESS, companySearchResponseList);
    }

    /**
     * 기업 기본정보 조회
     * @param id
     * @return
     */
    @GetMapping("/info/{companyId}")
    public ResponseEntity<Object> companyInfo(@PathVariable("companyId") Integer id) {
        CompanyResponse companyResponseList = companyService.findCompany(id);
        return SuccessResponse.createSuccess(SuccessCode.FIND_BASE_SUCCESS, companyResponseList);
    }


    /**
     * 기업 스크랩 여부
     * @param id
     * @return
     */
    @GetMapping("/scrap/{companyId}")
    public ResponseEntity<Object> companyScrap(@PathVariable("companyId") Integer id) {
        boolean scrapResult = companyService.isCompanyScraped(id, authUtil.getLoginUserId());
        return SuccessResponse.createSuccess(SuccessCode.FIND_SCRAP_SUCCESS, scrapResult);
    }


    /**
     * 기업 재무제표 조회
     * @param id
     * @return
     */

    @GetMapping("/statement/{companyId}")
    public ResponseEntity<Object> companyStatement(@PathVariable("companyId") Integer id) {
        List<CompanyStatementResponse> companyStatementResponseList = companyService.findCompanyStatement(id);
        return SuccessResponse.createSuccess(SuccessCode.FIND_STATEMENT_SUCCESS, companyStatementResponseList);
    }

    /**
     * 기업 재무비율 조회
     * @param id
     * @return
     */
    @GetMapping("/rate/{companyId}")
    public ResponseEntity<Object> companyRate(@PathVariable("companyId") Integer id) {
        List<CompanyRateResponse> companyRateResponseList = companyService.findCompanyRate(id);
        return SuccessResponse.createSuccess(SuccessCode.FIND_RATE_SUCCESS, companyRateResponseList);
    }


    /**
     * 기업 청년친화 조회
     * @param id
     * @return
     */
    @GetMapping("/youthcompany/{companyId}")
    public ResponseEntity<Object> companyYouth(@PathVariable("companyId") Integer id) {
        List<CompanyYouthResponse> companyYouthResponseList = companyService.findCompanyYouth(id);
        return SuccessResponse.createSuccess(SuccessCode.FIND_COMPANYYOUTH_SUCCESS, companyYouthResponseList);
    }

    /**
     * 기업 분기정보 조회
     * @param id
     * @return
     */
    @GetMapping("/quarter/{companyId}")
    public ResponseEntity<Object> companyQuarterInfo(@PathVariable("companyId") Integer id) {
        List<CompanyQuarterInfoResponse> companyQuarterInfoResponseList = companyService.findCompanyQuarterInfo(id);
        return SuccessResponse.createSuccess(SuccessCode.FIND_COMPANYYQUARTERINFO_SUCCESS, companyQuarterInfoResponseList);
    }

    /**
     * 기업 top10 정보 조회
     * @return
     */
    @GetMapping("/popular")
    public ResponseEntity<Object> getPopularCompanies() {
        List<CompanyResponse> popularCompanyResponseList = companyService.getPopularCompanies();
        return SuccessResponse.createSuccess(SuccessCode.FIND_COMPANYPOPULAR_SUCCESS, popularCompanyResponseList);
    }


    /**
     * 관심기업 스크랩
     * @param id
     * @return
     */
    @PostMapping("/scrap/{companyId}")
    public ResponseEntity<Object> companyScrapCreate(@PathVariable("companyId") Integer id) {
        companyService.companyScrap(id, authUtil.getLoginUserId());
        return SuccessResponse.createSuccess(SuccessCode.CREATE_SCRAP_SUCCESS);
    }



    /**
     * 관심기업 스크랩 취소
     * @param id
     * @return
     */
    @DeleteMapping("/scrap/{companyId}")
    public ResponseEntity<Object> companyScrapCancel(@PathVariable("companyId") Integer id) {
        companyService.companyScrapCancel(id, authUtil.getLoginUserId());
        return SuccessResponse.createSuccess(SuccessCode.CANCEL_SCRAP_SUCCESS);
    }

}
