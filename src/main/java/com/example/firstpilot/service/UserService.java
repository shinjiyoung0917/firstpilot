package com.example.firstpilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.firstpilot.model.User;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /* 회원가입 */
    public void join(User user) {
        log.info("서비스 로그 - 회원가입");

        // 이메일 인증
        // 이메일, 비밀번호 암호화
        // 최초 닉네임 임시 생성 (중복 불가)


    }

    /* 닉네임 변경 */
    public void changeNickname() {
        // 닉네임 중복 불가

    }


}
