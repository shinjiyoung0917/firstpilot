package com.example.firstpilot.controller;

import com.example.firstpilot.dto.MailAuthDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Member;
import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.dto.MemberDto;
import com.example.firstpilot.service.MemberService;
import com.example.firstpilot.repository.MemberRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;
import java.text.ParseException;

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
    public MailAuth postAuthKey(@RequestBody MailAuthDto mailAuthDto) {
        log.info("postAuthKey 로그 - 진입");
        log.info("postAuthKey 로그 - data : " + mailAuthDto.getEmail());

        return this.memberService.createAuthKey(mailAuthDto);
    }

    /* 회원가입 요청 */
    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Member postMember(@RequestBody MemberDto memberDto) {
        log.info("postMember 로그 - 진입");
        log.info("postMember 로그 - data1 : " + memberDto.getEmail());
        log.info("postMember 로그 - data2 : " + memberDto.getPassword());
        return this.memberService.createMember(memberDto);
    }

    /* 회원정보 요청 */
    @GetMapping("/members")
    public Member getMember(@RequestParam Long id) {
        log.info("getMember 로그 - 진입");

        //List<Member> member = new ArrayList<>();
        //memberRepo.findAll().forEach(members::add);
        return this.memberRepo.findByMemberId(id);
    }

    /* 회원정보(닉네임) 수정 요청 */
    @PutMapping("/members")
    public Member putMember(@RequestBody MemberDto memberDto) {
        log.info("putMember 로그 - 진입");
        try {
            return this.memberService.updateMember(memberDto);
        } catch(ParseException e) {
            log.info("putMember 로그 - 에러 : " + e);
            return null;
        }
    }

    /* 세션 값 요청 */
    @GetMapping("/session")
    public Member getSession() {
        log.info("getSession 로그 - 진입");
        return this.memberService.readSession();
    }

    /* 회원탈퇴 요청 */
    @DeleteMapping("/members")
    public void deleteMember() {
        this.memberService.deleteMember();
    }
}
