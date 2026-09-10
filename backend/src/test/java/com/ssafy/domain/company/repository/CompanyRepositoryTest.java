package com.ssafy.domain.company.entity;

import com.ssafy.domain.company.repository.CompanyRepository;
import com.ssafy.domain.company.repository.CompanyScrapRepository;
import com.ssafy.domain.users.entity.Users;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyScrapRepository companyScrapRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void incrementsViewsInTheDatabase() {
        Company company = companyRepository.saveAndFlush(company("테스트기업", 0L));

        int updatedRows = companyRepository.incrementViews(company.getId());

        assertThat(updatedRows).isOne();
        assertThat(companyRepository.findById(company.getId()).orElseThrow().getViews()).isEqualTo(1L);
    }

    @Test
    void repeatedIncrementsAccumulateWithoutLoadingTheEntity() {
        Company company = companyRepository.saveAndFlush(company("반복조회기업", 0L));

        companyRepository.incrementViews(company.getId());
        companyRepository.incrementViews(company.getId());

        assertThat(companyRepository.findById(company.getId()).orElseThrow().getViews()).isEqualTo(2L);
    }

    @Test
    void rejectsDuplicateScrapsForTheSameUserAndCompany() {
        Company company = companyRepository.saveAndFlush(company("스크랩기업", 0L));
        Users user = new Users();
        ReflectionTestUtils.setField(user, "email", "member@example.com");
        ReflectionTestUtils.setField(user, "password", "encoded-password");
        entityManager.persist(user);
        entityManager.flush();

        companyScrapRepository.saveAndFlush(CompanyScrap.createCompanyScrap(company, user));

        assertThatThrownBy(() -> companyScrapRepository.saveAndFlush(
                CompanyScrap.createCompanyScrap(company, user)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Company company(String name, Long views) {
        Company company = new Company();
        ReflectionTestUtils.setField(company, "name", name);
        ReflectionTestUtils.setField(company, "views", views);
        return company;
    }
}
