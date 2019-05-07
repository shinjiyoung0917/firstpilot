package com.example.firstpilot.model;

import com.example.firstpilot.util.AuthType;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.dto.MailAuthDto;
import com.example.firstpilot.util.CurrentTime;
import com.example.firstpilot.util.MailAuthPK;

import javax.persistence.*;

import java.util.Random;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(MailAuthPK.class)
@Table(name = "mail_auth")
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

    public MailAuthDto toDto() {
        return MailAuthDto.builder()
                .email(email)
                .authType(authType)
                .authKey(authKey)
                .createdDate(createdDate)
                .build();
    }

    public MailAuth updateMailAuthEntity(MailAuthDto mailAuthDto) {

        return this;
    }

    private static final Logger log = LoggerFactory.getLogger(MailAuth.class);
    public String encryptEmail() {
        log.info("encryptEmail 로그 - 진입");
        Member member = Member.builder().build();
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
