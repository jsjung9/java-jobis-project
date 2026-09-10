package com.ssafy.domain.common.embeddable;

import com.ssafy.domain.users.dto.request.TendencyRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FactorTest {

    @Test
    void preservesEachIndependentTendencyScore() {
        TendencyRequest request = new TendencyRequest(1.0, 2.0, 3.0, 4.0, 5.0);

        Factor factor = Factor.createFactor(request);

        assertThat(factor.getStability()).isEqualTo(1.0);
        assertThat(factor.getProfitability()).isEqualTo(2.0);
        assertThat(factor.getActivity()).isEqualTo(3.0);
        assertThat(factor.getGrowth()).isEqualTo(4.0);
        assertThat(factor.getEfficiency()).isEqualTo(5.0);
    }
}
