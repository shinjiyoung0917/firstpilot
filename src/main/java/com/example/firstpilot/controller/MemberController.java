package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.dto.MemberDto;
import com.example.firstpilot.dto.MailAuthDto;
import com.example.firstpilot.service.MemberService;
import com.example.firstpilot.service.MailAuthService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;

@RestController
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private MailAuthService mailAuthService;

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public MailAuthDto postAuthKey(@RequestBody @Valid MailAuthDto mailAuthDto) {
        log.info("postAuthKey 로그 - 진입");
        log.info("postAuthKey 로그 - data : " + mailAuthDto.getEmail());

        return mailAuthService.createAuthKey(mailAuthDto);
    }

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDto postMember(@RequestBody @Valid MemberDto memberDto) {
        log.info("postMember 로그 - 진입");
        log.info("postMember 로그 - data1 : " + memberDto.getEmail());
        log.info("postMember 로그 - data2 : " + memberDto.getPassword());

        return memberService.createMember(memberDto);
    }

    @GetMapping("/session")
    public MemberDto getSession() {
        log.info("getSession 로그 - 진입");

        return memberService.readSession().toDto();
    }

    /* 닉네임 변경 가능 여부 확인 요청 */
    @GetMapping("/members/{memberId}/change-period")
    public ResponseEntity<Boolean> getMemberNicknameChangePeriod(@PathVariable("memberId") Long memberId) {
        log.info("getMember 로그 - 진입");

        boolean exceedOneWeek = memberService.readMemberNicknameChangePeriod(memberId);
        return new ResponseEntity<>(exceedOneWeek, HttpStatus.OK);
    }

    @PutMapping("/members")
    public MemberDto putMember(@RequestBody MemberDto memberDto) {
        log.info("putMember 로그 - 진입");

        return memberService.updateMember(memberDto);
    }

    @DeleteMapping("/members")
    public void deleteMember() {
        memberService.deleteMember();
    }
}
