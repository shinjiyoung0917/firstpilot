package com.example.firstpilot.model;

import com.example.firstpilot.dto.MemberDto;
import com.example.firstpilot.util.CurrentTime;
import com.example.firstpilot.util.EncryptSHA256;

import lombok.*;

import javax.persistence.*;

import org.springframework.security.crypto.password.PasswordEncoder;

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
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        updatedDate = currentTimeString;
        nickname = memberDto.getNickname();
        return this;
    }

    public String encryptEmail() {
        EncryptSHA256 sha = new EncryptSHA256();
        email = sha.encryptSHA256(email);
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
