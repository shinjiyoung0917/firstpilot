package com.example.firstpilot.service;

import com.example.firstpilot.model.User;
import com.example.firstpilot.model.AuthMail;
import com.example.firstpilot.repository.UserRepository;
import com.example.firstpilot.repository.AuthMailRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthMailRepository authRepo;
    @Autowired
    private JavaMailSender mailSender;

    private boolean lowerCheck;
    private int size;

    /* 이메일 인증코드 삽입 */
    public void createAuthCode(AuthMail authMailData) {
        log.info("createAuthCode 로그 - 진입");

        SimpleMailMessage message = new SimpleMailMessage();

        String text = getKey(50, false);
        log.info("createAuthCode 로그 - 코드 : " + text);

        /*
        message.setTo(userEmail);
        message.setSubject("[익명게시판] 이메일 인증 코드 발송");
        message.setText(text);
        */
        //mailSender.send(message);

        AuthMail authMail = new AuthMail();
        authMail.setEmail(authMailData.getEmail());
        authMail.setAuthType(1);
        authMail.setAuthKey(text);
        authMail.setCreatedDate(LocalDateTime.now());
        log.info("createAuthCode 로그 - data1 : " + authMail.getEmail());
        log.info("createAuthCode 로그 - data2 : " + authMail.getAuthType());
        log.info("createAuthCode 로그 - data3 : " + authMail.getAuthKey());
        log.info("createAuthCode 로그 - data4 : " + authMail.getCreatedDate());
        //return this.authRepo.save(authMail);
        log.info("createAuthCode 로그 - data4 : " + this.authRepo.save(authMail));
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
    public User createUser(User userData) {
        log.info("createUser 로그 - 진입");

        String email = userData.getEmail();
        String password = userData.getPassword();
        String emailDxpText = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        String passwordDxpText = "^[A-Za-z0-9]{6,12}$";

        if(Pattern.matches(emailDxpText, email)) {
            log.info("signUp 로그 - 올바른 이메일 패턴");
            if(Pattern.matches(passwordDxpText, password)) {
                log.info("signUp 로그 - 올바른 비밀번호 패턴");

                User user = new User();
                user.setEmail(userData.getEmail());
                user.setPassword(userData.getPassword());
                user.setNickname("test_nickname");
                return this.userRepo.save(user);
            } else {
                log.info("signUp 로그 - 잘못된 비밀번호 패턴");
                return null;
            }
        } else {
            log.info("signUp 로그 - 잘못된 이메일 패턴");
            return null;
        }

        // 정규식 검사
        // 이메일 중복 검사
        // 이메일, 비밀번호 암호화 후 DB에 삽입
        // 최초 닉네임 임시 생성 (중복 불가)
    }

    /* 닉네임 변경 */
    public void changeNickname() {
        // 닉네임 중복 불가
        // 이전에 변경 후 1주일 이내 변경 불가

    }


}
