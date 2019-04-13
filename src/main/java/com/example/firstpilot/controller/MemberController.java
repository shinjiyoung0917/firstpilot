package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Member;
import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.service.MemberService;
import com.example.firstpilot.repository.MemberRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepo;

    /* 이메일 인증코드 생성 요청 */
    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public MailAuth postAuthKey(@RequestBody MailAuth mailAuth) {
        log.info("postAuthKey 로그 - 진입");
        log.info("postAuthKey 로그 - data : " + mailAuth.getEmail());
        return this.memberService.createAuthKey(mailAuth);
    }

    /* 회원가입 요청 */
    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Member postMember(@RequestBody Member member) {
        log.info("postMember 로그 - 진입");
        log.info("postMember 로그 - data1 : " + member.getEmail());
        log.info("postMember 로그 - data2 : " + member.getPassword());
        return this.memberService.createMember(member);
    }

    /* 사용자 정보 요청 */
    @GetMapping("/members")
    public Member getMember(@RequestParam Long id) {
        log.info("getMember 로그 - 진입");

        //List<Member> member = new ArrayList<>();
        //memberRepo.findAll().forEach(members::add);
        return this.memberRepo.findByMemberId(id);
    }

  /* 세션 값 요청 */
    @GetMapping("/session")
    public Member getSession() {
        log.info("getSession 로그 - 진입");
        return this.memberService.readSession();
    }

}

