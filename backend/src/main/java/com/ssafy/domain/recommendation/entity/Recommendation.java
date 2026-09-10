package com.ssafy.domain.recommendation.entity;

import com.ssafy.domain.company.entity.Company;
import com.ssafy.domain.worldcup.entity.Worldcup;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "recommendation",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_recommendation_worldcup_company",
                columnNames = {"worldcup_id", "company_id"}
        ),
        indexes = {
                @Index(name = "idx_recommendation_worldcup", columnList = "worldcup_id"),
                @Index(name = "idx_recommendation_company", columnList = "company_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worldcup_id")
    private Worldcup worldcup;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}
