package com.example.firstpilot.dto;

import com.example.firstpilot.model.MailAuth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.PrePersist;

import com.example.firstpilot.util.CurrentTime;

@Getter
public class MailAuthDto {
    private String email;
    private Integer authType;
    private String authKey;
    private String createdDate;

    @PrePersist
    public void prePersist() {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        createdDate = currentTimeString;
    }

    public MailAuth toEntity() {
        return MailAuth.builder()
                .email(email)
                .authType(authType)
                .authKey(authKey)
                .createdDate(createdDate)
                .build();
    }


    /*public MailAuthDto(String email, Integer authType, String authKey, String createdDate) {
        this.email = email;
        this.authType = authType;
        this.authKey = authKey;
        this.createdDate = createdDate;
    }*/
}
