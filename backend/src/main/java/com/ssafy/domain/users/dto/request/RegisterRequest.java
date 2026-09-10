package com.ssafy.domain.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Size(max = 254, message = "이메일은 254자 이하여야 합니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    @Size(min = 8, max = 72, message = "비밀번호는 8자 이상 72자 이하여야 합니다")
    private String password;

}
