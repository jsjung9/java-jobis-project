package com.ssafy.domain.company.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.domain.company.entity.Company;
import com.ssafy.domain.company.entity.CompanyScrap;
import com.ssafy.domain.company.repository.*;
import com.ssafy.domain.users.entity.Users;
import com.ssafy.domain.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock CompanyRepository companyRepository;
    @Mock CompanyYouthRepository companyYouthRepository;
    @Mock CompanyRateRepository companyRateRepository;
    @Mock CompanyQuarterInfoRepository companyQuarterInfoRepository;
    @Mock CompanyStatementRepository companyStatementRepository;
    @Mock CompanyScrapRepository companyScrapRepository;
    @Mock UserRepository userRepository;
    @Mock JPAQueryFactory jpaQueryFactory;

    @InjectMocks
    CompanyService companyService;

    @Test
    void repeatedScrapRequestIsIdempotent() {
        Company company = mock(Company.class);
        Users user = mock(Users.class);
        when(companyRepository.findById(10)).thenReturn(Optional.of(company));
        when(userRepository.findById(20)).thenReturn(Optional.of(user));
        when(companyScrapRepository.existsByCompanyAndUser(company, user)).thenReturn(true);

        companyService.companyScrap(10, 20);

        verify(companyScrapRepository, never()).save(any(CompanyScrap.class));
    }
}
