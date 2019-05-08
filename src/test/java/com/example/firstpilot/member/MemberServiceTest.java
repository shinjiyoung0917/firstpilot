package com.example.firstpilot.member;

import com.example.firstpilot.model.Member;
import com.example.firstpilot.dto.MemberDto;
import com.example.firstpilot.service.MemberService;
import com.example.firstpilot.repository.MemberRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.example.firstpilot.exceptionAndHandler.NotFoundResourcesException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@TestPropertySource(properties = {"spring.mail.username=test@test.com"})
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepo;

    @Test
    @WithMockUser
    public void 닉네임_수정_테스트() {
        Member member = Member.builder()
                .email("test_email@test.com")
                .nickname("test_nickname")
                .password("test123")
                .build();
        member = memberRepo.save(member);

        if(memberService.possibleToChangeNickname(member.getUpdatedDate())) {
            MemberDto memberDto = MemberDto.builder()
                    .nickname("test_modify_nickname")
                    .build();
            memberRepo.save(member.updateMemberEntity(memberDto));

            Member foundMember = memberRepo.findByMemberId(member.getMemberId())
                    .orElseThrow(() ->  new NotFoundResourcesException("존재하지 않는 회원입니다."));

            assertThat(member.getNickname())
                    .isEqualTo(foundMember.getNickname());
        }
    }



}
