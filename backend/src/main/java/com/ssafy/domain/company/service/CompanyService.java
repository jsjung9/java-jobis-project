package com.ssafy.domain.company.service;

import static com.ssafy.domain.company.entity.QCompany.company;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.domain.company.dto.*;
import com.ssafy.domain.company.dto.response.*;
import com.ssafy.domain.company.entity.*;
import com.ssafy.domain.company.exception.CompanyNotFoundException;
import com.ssafy.domain.company.repository.*;
import com.ssafy.domain.users.entity.Users;
import com.ssafy.domain.users.exception.UserNotFoundException;
import com.ssafy.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyYouthRepository companyYouthRepository;
    private final CompanyRateRepository companyRateRepository;
    private final CompanyQuarterInfoRepository companyQuarterInfoRepository;
    private final CompanyStatementRepository companyStatementRepository;

    private final CompanyScrapRepository companyScrapRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;


    /**
     * 기업 검색
     * @param pageable
     * @param keyword
     * @param location
     * @param type
     * @return
     */

    public Page<CompanySearchResponse> getCompanies(Pageable pageable, String keyword, List<String> location, List<String> type) {
        List<CompanySearchResponse> companyList = jpaQueryFactory
                .selectFrom(company)
                .where(
                        containsKeyword(keyword),
                        inLocation(location),
                        inType(type))
                .orderBy(company.name.asc(), company.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(CompanySearchDtoMapper::companySearchResponse)
                .toList();

        Long totalCount = jpaQueryFactory
                .select(company.count())
                .from(company)
                .where(
                        containsKeyword(keyword),
                        inLocation(location),
                        inType(type))
                .fetchOne();

        return new PageImpl<>(companyList, pageable, totalCount == null ? 0L : totalCount);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return StringUtils.hasText(keyword) ? company.name.containsIgnoreCase(keyword.trim()) : null;
    }

    private BooleanExpression inLocation(List<String> locations) {
        if (locations != null && !locations.isEmpty()) {
            BooleanExpression[] expressions = locations.stream()
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .map(company.address::contains)
                    .toArray(BooleanExpression[]::new);
            return expressions.length == 0 ? null : com.querydsl.core.types.dsl.Expressions.anyOf(expressions);
        }
        return null;
    }

    private BooleanExpression inType(List<String> types) {
        if (types != null && !types.isEmpty()) {
            BooleanExpression[] expressions = types.stream()
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .map(company.type::contains)
                    .toArray(BooleanExpression[]::new);
            return expressions.length == 0 ? null : com.querydsl.core.types.dsl.Expressions.anyOf(expressions);
        }
        return null;
    }


    /**
     * 기업 기본정보 조회
     * @param id
     * @return
     */
    @Transactional
    public CompanyResponse findCompany(Integer id) {
        if (companyRepository.incrementViews(id) == 0) {
            throw new CompanyNotFoundException();
        }
        return companyRepository.findById(id)
                .map(CompanyDtoMapper::companyEntityToDto)
                .orElseThrow(CompanyNotFoundException::new);
    }


    /**
     * 기업스크랩 여부
     * @param companyId
     * @param userId
     * @return
     */
    public boolean isCompanyScraped(Integer companyId, Integer userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFoundException::new);

        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return companyScrapRepository.existsByCompanyAndUser(company, user);
    }


    /**
     * 기업 재무제표 조회
     * @param id
     * @return
     */
    public List<CompanyStatementResponse> findCompanyStatement(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        List<CompanyStatement> companyStatements = companyStatementRepository.findAllByCompany(company);
        return CompanyStatementDtoMapper.companyStatementEntityToDtoList(companyStatements);
    }


    /**
     * 기업 재무비율 조회
     * @param id
     * @return
     */
    public List<CompanyRateResponse> findCompanyRate(Integer id){
        Company company = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        List<CompanyRate> companyRates = companyRateRepository.findAllByCompany(company);
        return CompanyRateDtoMapper.companyRateEntityToDtoList(companyRates);
    }


    /**
     * 기업 청년친화 조회
     * @param id
     * @return
     */
    public List<CompanyYouthResponse> findCompanyYouth(Integer id){
        Company company = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        List<CompanyYouth> companyYouth = companyYouthRepository.findAllByCompany(company);
        return CompanyYouthDtoMapper.companyYouthEntityToDtoList(companyYouth);
    }


    /**
     * 기업 분기정보 조회
     * @param id
     * @return
     */
    public List<CompanyQuarterInfoResponse> findCompanyQuarterInfo(Integer id){
        Company company = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        List<CompanyQuarterInfo> companyQuarterInfo = companyQuarterInfoRepository.findAllByCompany(company);
        return CompanyQuarterInfoDtoMapper.companyQuarterInfoToDtoList(companyQuarterInfo);
    }


    /**
     * 기업 top10 정보 조회
     * @return
     */

    public List<CompanyResponse> getPopularCompanies() {
        List<Company> popularCompanyInfo = companyRepository.findByOrderByViewsDesc(Limit.of(10));
        return CompanyDtoMapper.companyEntityToDtoList(popularCompanyInfo);
    }




    /**
     * 관심기업 스크랩
     * @param companyId
     * @param userId
     */
    @Transactional
    public void companyScrap(Integer companyId, Integer userId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFoundException::new);
        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!companyScrapRepository.existsByCompanyAndUser(company, user)) {
            companyScrapRepository.save(CompanyScrap.createCompanyScrap(company, user));
        }
    }


    /**
     * 관심기업 스크랩 취소
     * @param companyId
     * @param userId
     */
    @Transactional
    public void companyScrapCancel(Integer companyId, Integer userId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFoundException::new);
        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        companyScrapRepository.deleteCompanyScrapByCompanyAndUser(company, user);
    }

}
