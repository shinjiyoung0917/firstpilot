package com.example.firstpilot.dto;

import com.example.firstpilot.model.MailAuth;

import com.example.firstpilot.util.AuthType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;

import com.example.firstpilot.util.CurrentTime;

@Getter
@NoArgsConstructor
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

    public MailAuthDto(AuthType authType, String authKey, String createdDate) {
        this.authType = authType;
        this.authKey = authKey;
        // TODO: 필요없는 것 같으니 지우기
        this.createdDate = createdDate;
    }
}
