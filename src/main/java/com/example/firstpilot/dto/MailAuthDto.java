package com.example.firstpilot.dto;

import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.util.AuthType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailAuthDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private AuthType authType;
    private String authKey;
    private String createdDate;

    public MailAuth toEntity() {
        return MailAuth.builder()
                .email(email)
                .authType(authType)
                .authKey(authKey)
                .build();
    }
}
