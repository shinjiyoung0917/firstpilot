package com.example.firstpilot.model;

import com.example.firstpilot.util.CurrentTime;
import com.example.firstpilot.util.MailAuthPK;

import javax.persistence.*;

import lombok.Builder;
import lombok.Data;

import java.util.Random;

@Data
@Entity
@IdClass(MailAuthPK.class)
@Table(name = "mail_auth")
public class MailAuth {
    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Id
    @Column(name = "auth_type", nullable = false)
    private Integer authType;

    @Column(name = "auth_key", nullable = false)
    private String authKey;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    //db컬럼이 아니다라는거 명시

    @Builder
    public MailAuth(String email, Integer authType, String authKey, String createdDate) {
        this.email = email;
        this.authType = authType;
        this.authKey = authKey;
        this.createdDate = createdDate;
    }

    public void setMailAuthInfo(String email, String key) {
        this.email = email;
        this.authKey = key;
    }

    public String encryptEmail() {
        Member member = null;
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
