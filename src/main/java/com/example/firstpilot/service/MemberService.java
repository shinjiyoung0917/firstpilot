package com.example.firstpilot.service;

import com.example.firstpilot.model.Member;
import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.repository.MemberRepository;
import com.example.firstpilot.repository.MailAuthRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Pattern;


//@Transactional
@Service
public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private MailAuthRepository authRepo;
    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder emailAndPasswordEncoder = new BCryptPasswordEncoder();

    private boolean lowerCheck;
    private int size;

    /* 이메일 인증코드 삽입 */
    public MailAuth createAuthKey(MailAuth mailAuthData) {
        log.info("createAuthKey 로그 - 진입");

        // 이메일 중복 검사
        String email = mailAuthData.getEmail();
        String encodedEmail = emailAndPasswordEncoder.encode(email);
        Member isMailExist = this.memberRepo.findByEmail(email);
        if(isMailExist == null) {
            SimpleMailMessage message = new SimpleMailMessage();
            String text = getKey(50, false);

            /*
            message.setTo(memberEmail);
            message.setSubject("[익명게시판] 이메일 인증 코드 발송");
            message.setText(text);
            */
            //mailSender.send(message);

            MailAuth mailAuth = new MailAuth();
            mailAuth.setEmail(encodedEmail);
            mailAuth.setAuthType(1);
            mailAuth.setAuthKey(text);
            mailAuth.setCreatedDate(LocalDateTime.now());
            return this.authRepo.save(mailAuth);
        } else {
            mailAuthData.setEmail("Already-Exist");
            return mailAuthData;
        }
    }

    /* 인증코드 생성 */
    public String getKey(int size, boolean lowerCheck) {
        this.size = size;
        this.lowerCheck = lowerCheck;

        Random randoms = new Random();
        StringBuffer strBuff = new StringBuffer();
        int num = 0;
        do {
            num = randoms.nextInt(75)+48;
            if((num>=48 && num<=57) || (num>=65 && num<=90) || (num>=97 && num<=122)) {
                strBuff.append((char)num);
            }else {
                continue;
            }
        } while (strBuff.length() < size);
        if(lowerCheck) {
            return strBuff.toString().toLowerCase();
        }
        return strBuff.toString();
    }

    /* 회원가입 */
    public Member createMember(Member memberData) {
        log.info("createMember 로그 - 진입");

        String email = memberData.getEmail();
        String password = memberData.getPassword();
        String encodedEmail = emailAndPasswordEncoder.encode(email);
        String encodedPassword = emailAndPasswordEncoder.encode(password);

        // 이메일 중복 검사
        Member isMailExist = this.memberRepo.findByEmail(encodedEmail);
        if(isMailExist == null) {
            // 임시 닉네임 생성 및 중복 검사
            Random randoms = new Random();
            StringBuffer strBuff = new StringBuffer();
            String nickname = "";
            Member isNicknameExist = null;
            do {
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
                isNicknameExist = this.memberRepo.findByNickname(nickname);
            } while (isNicknameExist != null);

            // 정규식 검사
            String emailDxpText = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
            String passwordDxpText = "^[A-Za-z0-9]{6,12}$";

            if (Pattern.matches(emailDxpText, email)) {
                log.info("signUp 로그 - 올바른 이메일 패턴");
                if (Pattern.matches(passwordDxpText, password)) {
                    log.info("signUp 로그 - 올바른 비밀번호 패턴");

                    Member member = new Member();
                    member.setEmail(encodedEmail);
                    member.setPassword(encodedPassword);
                    member.setNickname(nickname);
                    return this.memberRepo.save(member);
                } else {
                    log.info("signUp 로그 - 잘못된 비밀번호 패턴");
                    return null;
                }
            } else {
                log.info("signUp 로그 - 잘못된 이메일 패턴");
                return null;
            }
        } else {
            memberData.setEmail("Already-Exist");
            return memberData;
        }

        // 이메일, 비밀번호 암호화

    }

    /* 닉네임 변경 */
    public void updateNickname() {
        // 닉네임 중복 불가
        // 이전에 변경 후 1주일 이내 변경 불가

    }


}
