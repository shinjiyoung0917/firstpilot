package com.example.firstpilot.model;

import com.example.firstpilot.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import javax.persistence.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Random;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member { //implements UserDetails
    @Id
    @GeneratedValue //(strategy = GenerationType.IDENTITY)
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
   /* @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "email")
    private List<MemberRole> role;*/

   @PrePersist
   public void prePersist() {

   }

   @Builder
    public Member(String email, String nickname, String password, String updatedDate) {
       this.email = email;
       this.password = nickname;
       this.password = password;
       this.updatedDate = updatedDate;
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

        String generatedNickname = "";

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
        generatedNickname = strBuff.toString();
        return generatedNickname;
    }

    public void update(MemberDto memberDto) {
        this.email = memberDto.getEmail();
        this.nickname = memberDto.getNickname();
        this.password = memberDto.getPassword();
        this.updatedDate = memberDto.getUpdatedDate();
    }
}
