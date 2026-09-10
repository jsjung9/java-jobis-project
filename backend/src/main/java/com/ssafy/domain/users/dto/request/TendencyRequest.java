package com.ssafy.domain.users.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TendencyRequest {

    @NotNull
    @DecimalMin("-9.0")
    @DecimalMax("9.0")
    private Double stability;

    @NotNull
    @DecimalMin("-9.0")
    @DecimalMax("9.0")
    private Double profitability;

    @NotNull
    @DecimalMin("-9.0")
    @DecimalMax("9.0")
    private Double activity;

    @NotNull
    @DecimalMin("-9.0")
    @DecimalMax("9.0")
    private Double growth;

    @NotNull
    @DecimalMin("-9.0")
    @DecimalMax("9.0")
    private Double efficiency;

}
