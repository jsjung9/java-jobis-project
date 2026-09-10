package com.ssafy.domain.users.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsMalformedEmailAndShortPassword() {
        RegisterRequest request = new RegisterRequest("not-an-email", "short");

        assertThat(validator.validate(request)).hasSize(2);
    }

    @Test
    void rejectsTendencyScoresOutsideTheDomainScale() {
        TendencyRequest request = new TendencyRequest(10.0, 0.0, 0.0, 0.0, 0.0);

        assertThat(validator.validate(request)).hasSize(1);
    }
}
