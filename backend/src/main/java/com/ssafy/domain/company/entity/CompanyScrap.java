package com.ssafy.domain.company.entity;

import com.ssafy.domain.users.entity.Users;
import com.ssafy.global.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "company_scrap",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_company_scrap_company_user",
                columnNames = {"company_id", "users_id"}
        ),
        indexes = @Index(name = "idx_company_scrap_user", columnList = "users_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyScrap extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_scrap_id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users user;

    public static CompanyScrap createCompanyScrap(Company company, Users user){
        CompanyScrap companyScrap = new CompanyScrap();
        companyScrap.company = company;
        companyScrap.user = user;
        return companyScrap;
    }
}
