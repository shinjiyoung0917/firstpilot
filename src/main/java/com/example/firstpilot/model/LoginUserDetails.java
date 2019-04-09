package com.example.firstpilot.model;


import lombok.Getter;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class LoginUserDetails extends User {
    private static final long serialVersionUID = 1L;

    @Getter
    private long memberId;

    public LoginUserDetails(Member member) {
        // 일반적으로는 AuthorityUtils.createAuthorityList 에 다수의 룰을 넣고
        // account 테이블과 분리되어 별도의 권한테이블을 join 해서 가져와야함
        super
                (
                        member.getEmail(),
                        member.getPassword(),
                        AuthorityUtils.createAuthorityList(member.getRole())
                );
        memberId = member.getMemberId();
    }

}
