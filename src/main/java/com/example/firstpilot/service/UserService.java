package com.example.firstpilot.service;

import com.example.firstpilot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.firstpilot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository repo;

    /* 이메일 인증코드 생성 */
    public void createAuthCode() {

    }

    /* 회원이 입력한 코드와 서버가 생성한 코드 비교 */
    public void checkAuthCode() {

    }

    /* 회원가입 */
    @Transactional
    public boolean signUp(User user) {
        log.info("signUp 로그 - 진입");

        String email = user.getEmail();
        String password = user.getPassword();
        String emailDxpText = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        String passwordDxpText = "^[A-Za-z0-9]{6,12}$";

        if(Pattern.matches(emailDxpText, email)) {
            log.info("signUp 로그 - 올바른 이메일 패턴");
            if(Pattern.matches(passwordDxpText, password)) {
                log.info("signUp 로그 - 올바른 비밀번호 패턴");

                User _user = new User();
                _user.setEmail(user.getEmail());
                _user.setEmail(user.getPassword());
                _user.setNickname("test_nickname");
                repo.save(_user);
            } else {
                log.info("signUp 로그 - 잘못된 비밀번호 패턴");
            }
        } else {
            log.info("signUp 로그 - 잘못된 이메일 패턴");

            return false;
        }

        // 정규식 검사
        // 이메일 중복 검사
        // 이메일, 비밀번호 암호화 후 DB에 삽입
        // 최초 닉네임 임시 생성 (중복 불가)

        return true;
    }

    /* 닉네임 변경 */
    public void changeNickname() {
        // 닉네임 중복 불가
        // 이전에 변경 후 1주일 이내 변경 불가

    }


}
