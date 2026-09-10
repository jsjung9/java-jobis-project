package com.ssafy.global.response.structure;

import com.ssafy.global.response.code.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void includesFieldErrorsInValidationResponse() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "email", "올바른 이메일 형식이어야 합니다."));

        ErrorResponse response =
                ErrorResponse.of(new BindException(bindingResult), ErrorCode.INVALID_PARAMETER);

        assertThat(response.getCode()).isEqualTo("INVALID_PARAMETER");
        assertThat(response.getErrors())
                .containsEntry("email", "올바른 이메일 형식이어야 합니다.");
    }
}
