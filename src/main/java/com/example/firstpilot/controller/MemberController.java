package com.example.firstpilot.controller;

import com.example.firstpilot.model.Member;
import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.repository.MemberRepository;
import com.example.firstpilot.service.MemberService;
//import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private MemberService memberService;

    /* 이메일 인증코드 생성하기 */
    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public MailAuth postAuthKey(@RequestBody MailAuth mailAuth) {
        log.info("postAuthKey 로그 - 진입");
        log.info("postAuthKey 로그 - data : " + mailAuth.getEmail());
        return memberService.createAuthKey(mailAuth);
    }

    /* 사용자 정보 삽입하기 */
    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Member postMember(@RequestBody Member member) {
        log.info("postMember 로그 - 진입");
        log.info("postMember 로그 - data1 : " + member.getEmail());
        log.info("postMember 로그 - data2 : " + member.getPassword());
        return memberService.createMember(member);
    }

    /* 사용자 정보 가져오기 */
    @GetMapping("/members")
    public Member getMember(@RequestParam Long id) {
        log.info("getMember 로그 - 진입");

        //List<Member> member = new ArrayList<>();
        //memberRepo.findAll().forEach(members::add);
        return this.memberRepo.findByMemberId(id);
    }

}

