package com.example.firstpilot.model;

import com.example.firstpilot.dto.MemberDto;
import com.example.firstpilot.util.CurrentTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "updated_date")
    private String updatedDate;

    private String role;

    @PrePersist
    public void prePersist() {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        updatedDate = currentTimeString;
    }

    public MemberDto toDto() {
        return MemberDto.builder()
                .memberId(memberId)
                .email(email)
                .nickname(nickname)
                .password(password)
                .updatedDate(updatedDate)
                .build();
    }

    public Member updateMemberEntity(MemberDto memberDto) {
        nickname = memberDto.getNickname();
        return this;
    }

    /* SHA256 해시 함수 (이메일을 위한 것) */
    public String encryptSHA256(String email) {
        String SHA = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(email.getBytes());
            byte byteData[] = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; ++i){
                stringBuffer.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return SHA;
    }

    public String encryptEmail() {
        email = encryptSHA256(email);
        return email;
    }

    public String encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
        return password;
    }

    public String generateTemporaryNickname() {
        Random randoms = new Random();
        StringBuffer strBuff = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            int rIndex = randoms.nextInt(3);
            switch (rIndex) {
                case 0:
                    // A-Z
                    strBuff.append((char) ((int) (randoms.nextInt(26)) + 65));
                    break;
                case 1:
                    // 0-9
                    strBuff.append((randoms.nextInt(10)));
                    break;
            }
        }
        nickname = strBuff.toString();
        return nickname;
    }
}
