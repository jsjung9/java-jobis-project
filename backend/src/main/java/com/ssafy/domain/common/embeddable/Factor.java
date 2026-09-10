package com.ssafy.domain.common.embeddable;

import java.util.Objects;

import com.ssafy.domain.users.dto.request.TendencyRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Factor {

    @NotNull
    @Column(name = "stability")
    private double stability;

    @NotNull
    @Column(name = "profitability")
    private double profitability;

    @NotNull
    @Column(name = "activity")
    private double activity;

    @NotNull
    @Column(name = "growth")
    private double growth;

    @NotNull
    @Column(name = "efficiency")
    private double efficiency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factor factor = (Factor) o;
        return Double.compare(stability, factor.stability) == 0 && Double.compare(profitability, factor.profitability) == 0 && Double.compare(activity, factor.activity) == 0 && Double.compare(growth, factor.growth) == 0 && Double.compare(efficiency, factor.efficiency) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stability, profitability, activity, growth, efficiency);
    }

    public static Factor createFactor(TendencyRequest tendencyRequest){
        Factor factor = new Factor();
        factor.stability = tendencyRequest.getStability();
        factor.profitability = tendencyRequest.getProfitability();
        factor.activity = tendencyRequest.getActivity();
        factor.growth = tendencyRequest.getGrowth();
        factor.efficiency = tendencyRequest.getEfficiency();
        return factor;
    }

}
