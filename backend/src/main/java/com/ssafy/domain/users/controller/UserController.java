package com.ssafy.domain.users.controller;

import com.ssafy.domain.company.dto.response.CompanyResponse;
import com.ssafy.domain.users.dto.request.TendencyRequest;
import com.ssafy.domain.users.dto.response.TendencyResponse;
import com.ssafy.domain.users.service.UserService;
import com.ssafy.global.response.code.SuccessCode;
import com.ssafy.global.response.structure.SuccessResponse;
import com.ssafy.global.util.AuthUtil;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Tag(name = "회원기업성향")
public class UserController {

    private final UserService userService;
    private final AuthUtil authUtil;

    /**
     * 회원 성향입력
     * @param tendencyRequest
     * @return
     */
    @PostMapping("/tendency")
    public ResponseEntity<Object> usersTendencyCreate(@Valid @RequestBody TendencyRequest tendencyRequest) {
        userService.applyUsersTendency(authUtil.getLoginUserId(), tendencyRequest);
        return SuccessResponse.createSuccess(SuccessCode.APPLY_TENDENCY_SUCCESS);
    }


    /**
     * 회원 성향조회
     * @return
     */

    @GetMapping("/tendency")
    public ResponseEntity<Object> usersTendency(){
        List<TendencyResponse> tendencyResponseList = userService.findTendency(authUtil.getLoginUser());
        return SuccessResponse.createSuccess(SuccessCode.FIND_TENDENCY_SUCCESS, tendencyResponseList);
    }


    /**
     * 회원 관심기업 조회
     * @return
     */
    @GetMapping("/scrap")
    public ResponseEntity<Object> usersScrapCompanyList(){
        List<CompanyResponse> companyResponseList = userService.findUserScrap(authUtil.getLoginUserId());
        return SuccessResponse.createSuccess(SuccessCode.FIND_SCRAPCOMPANY_SUCCESS, companyResponseList);
    }
}
