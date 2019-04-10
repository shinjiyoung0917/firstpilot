package com.example.firstpilot.model;

import lombok.Getter;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUserDetails extends User {
    private static final Logger log = LoggerFactory.getLogger(LoginUserDetails.class);

    private static final long serialVersionUID = 1L;

    @Getter
    private long memberId;

    public LoginUserDetails(Member member) {
        // 일반적으로는 AuthorityUtils.createAuthorityList 에 다수의 룰을 넣고
        // Member 테이블과 분리되어 별도의 Authority 테이블을 join 해서 가져와야함
        super
                (
                        member.getEmail(),
                        member.getPassword(),
                        AuthorityUtils.createAuthorityList(member.getRole())
                );

        memberId = member.getMemberId();
        log.info("LoginUserDetails 로그 - member : " + member.getEmail() + ", " + member.getPassword() + ", " + member.getMemberId());
    }

}
