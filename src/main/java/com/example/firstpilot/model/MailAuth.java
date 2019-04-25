package com.example.firstpilot.model;

import com.example.firstpilot.util.AuthType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.dto.MailAuthDto;
import com.example.firstpilot.util.CurrentTime;
import com.example.firstpilot.util.MailAuthPK;

import javax.persistence.*;

import lombok.Builder;
import lombok.Data;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@Entity
@IdClass(MailAuthPK.class)
@Table(name = "mail_auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailAuth {
    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Id
    @Column(name = "auth_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(name = "auth_key", nullable = false)
    private String authKey;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    @PrePersist
    public void prePersist() {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        createdDate = currentTimeString;
    }

    @Builder
    public MailAuth(String email, AuthType authType, String authKey, String createdDate) {
        this.email = email;
        this.authType = authType;
        this.authKey = authKey;
        this.createdDate = createdDate;
    }

    public void setEmailAndAuthTypeAndAuthKey(String encryptedEmail, AuthType authType, String authKey) {
        this.email = encryptedEmail;
        this.authType = authType;
        this.authKey = authKey;
    }

    public MailAuthDto toDto() {
        return new MailAuthDto(authType, authKey, createdDate);
    }

    public MailAuth updateMailAuthEntity(MailAuthDto mailAuthDto) {

        return this;
    }

    private static final Logger log = LoggerFactory.getLogger(MailAuth.class);
    public String encryptEmail() {
        log.info("encryptEmail 로그 - 진입");
        Member member = new Member(null, null, null, null);
        email = member.encryptSHA256(email);
        return email;
    }

    public String generateKey(int sizeParam, boolean lowerCheckParam) {
        int size = sizeParam;
        boolean lowerCheck = lowerCheckParam;

        Random randoms = new Random();
        StringBuffer strBuff = new StringBuffer();
        int num = 0;
        do {
            num = randoms.nextInt(75) + 48;
            if((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                strBuff.append((char)num);
            }else {
                continue;
            }
        } while (strBuff.length() < size);
        if(lowerCheck) {
            authKey = strBuff.toString().toLowerCase();
        }
        authKey = strBuff.toString();

       return authKey;
    }
}
