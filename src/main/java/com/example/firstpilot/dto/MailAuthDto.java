package com.example.firstpilot.dto;

import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.util.AuthType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailAuthDto {
    private String email;
    AuthType authType;
    private String authKey;
    private String createdDate;

    public MailAuth toEntity() {
        return MailAuth.builder()
                .email(email)
                .authType(authType)
                .authKey(authKey)
                .createdDate(createdDate)
                .build();
    }
}
