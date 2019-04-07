package com.example.firstpilot.service;

import com.example.firstpilot.model.User;
import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.repository.UserRepository;
import com.example.firstpilot.repository.MailAuthRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


//@Transactional
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MailAuthRepository authRepo;
    @Autowired
    private JavaMailSender mailSender;

    private boolean lowerCheck;
    private int size;

    /* 이메일 인증코드 삽입 */
    public MailAuth createAuthKey(MailAuth mailAuthData) {
        log.info("createAuthKey 로그 - 진입");

        SimpleMailMessage message = new SimpleMailMessage();

        String text = getKey(50, false);

        /*
        message.setTo(userEmail);
        message.setSubject("[익명게시판] 이메일 인증 코드 발송");
        message.setText(text);
        */
        //mailSender.send(message);

        MailAuth mailAuth = new MailAuth();
        mailAuth.setEmail(mailAuthData.getEmail());
        mailAuth.setAuthType(1);
        mailAuth.setAuthKey(text);
        mailAuth.setCreatedDate(LocalDateTime.now());
        return this.authRepo.save(mailAuth);
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

    /* 이메일과 인증타입에 따라 인증코드 조회 */
    public List<MailAuth> readAuthKey(MailAuth mailAuthData) {
        log.info("readAuthKey 로그 - 진입");

        List<MailAuth> mailAuth = this.authRepo.findByEmailAndAuthType(mailAuthData.getEmail(), mailAuthData.getAuthType());
        return mailAuth;
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
