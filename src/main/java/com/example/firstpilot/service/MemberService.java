package com.example.firstpilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.*;
import com.example.firstpilot.dto.MemberDto;
import com.example.firstpilot.repository.MemberRepository;
import com.example.firstpilot.repository.MailAuthRepository;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.repository.CommentRepository;
import com.example.firstpilot.util.LoginUserDetails;
import com.example.firstpilot.util.BlockStatus;
import com.example.firstpilot.util.CurrentTime;
import com.example.firstpilot.util.EncryptSHA256;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import com.example.firstpilot.exceptionAndHandler.*;

@Service
public class MemberService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private MailAuthRepository authRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private CommentRepository commentRepo;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private EncryptSHA256 sha = new EncryptSHA256();

    public MemberDto createMember(MemberDto memberDto) {
        log.info("createMember 로그 - 진입");

        Member member = memberDto.toEntity();

        String encryptedEmail = member.encryptEmail();
        String encryptedPassword = member.encryptPassword(passwordEncoder);

        String nickname = "";
        if(!checkDuplicatedEmail(encryptedEmail)) {
            boolean isNicknameExist = true;

            do {
                nickname = member.generateTemporaryNickname();
                if(!checkDuplicatedNickname(nickname)) {
                    isNicknameExist = false;
                }
            } while (isNicknameExist);

            member = Member.builder()
                    .email(encryptedEmail)
                    .nickname(nickname)
                    .password(encryptedPassword)
                    .role("ROLE_USER")
                    .build();
            return memberRepo.save(member).toDto();
        } else {
            throw new BadRequestFromMemberException("이미 존재하는 이메일입니다.");
        }
    }

    public boolean checkDuplicatedEmail(String encryptedEmail) {
        log.info("checkDuplicatedEmail 로그 - 진입");

        return memberRepo.findByEmail(encryptedEmail).isPresent();
    }

    private boolean checkDuplicatedNickname(String nickname) {
        log.info("checkDuplicatedNickname 로그 - 진입");

        return memberRepo.findByNickname(nickname).isPresent();
    }

    /* 로그인(security 인증) */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 로그 - email : " + username);

        String encryptedEmail = sha.encryptSHA256(username);
        Member member = memberRepo.findByEmail(encryptedEmail)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new LoginUserDetails(member);
    }

    public Member readSession() {
        log.info("readSession 로그 - 진입");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal().equals("anonymousUser")) {
            throw new NotLoginMemberException();
        } else {
            LoginUserDetails login = (LoginUserDetails) authentication.getPrincipal();
            log.info("readSession 로그 - member id : " + login.getMemberId());

            Long memberId = login.getMemberId();
            Member member = memberRepo.findByMemberId(memberId)
                    .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 회원입니다."));
            return member;
        }
    }

    /* 닉네임 변경 가능 여부 (단순히 닉네임 수정 버튼 눌렀을 때) */
    public Boolean readMemberNicknameChangePeriod(Long memberId) {
        Member member = memberRepo.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 회원입니다."));
        return possibleToChangeNickname(member.getUpdatedDate());
    }

    public MemberDto updateMember(MemberDto memberDto) {
        log.info("updateMember 로그 - 진입");

        String beforeChangingNickname = readSession().getNickname();
        Member member = memberRepo.findByNickname(beforeChangingNickname)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 회원입니다."));

        if(checkDuplicatedNickname(memberDto.getNickname())) {
            log.info("updateMember 로그 - 중복으로 인해 닉네임 변경 불가");

            throw new BadRequestFromMemberException("이미 존재하는 닉네임입니다.");
        }

        if(possibleToChangeNickname(memberDto.getUpdatedDate())) {
            return memberRepo.save(member.updateMemberEntity(memberDto)).toDto();
        } else {
            throw new BadRequestFromMemberException("닉네임을 변경할 수 있는 기간이 아닙니다.");
        }
    }

    /* 닉네임 변경가능한지의 여부 판단 (닉네임 수정란에 기입하고 수정 버튼 눌렀을 때) */
    public boolean possibleToChangeNickname(String updatedDate) {
        long period = -1;

        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        log.info("possibleToChangeNickname 로그 - 지금 시간(String) : " + currentTimeString);

        try {
            SimpleDateFormat dateFormat = currentTime.getDateFormat();
            Date current = dateFormat.parse(currentTimeString);
            log.info("possibleToChangeNickname 로그 - 지금 시간(Date) : " + current);

            if(updatedDate != null) {
                Date past = dateFormat.parse(updatedDate);
                log.info("possibleToChangeNickname 로그 - 이전에 업데이트 했던 시간(Date) : " + past);

                period = (current.getTime() - past.getTime()) / (24 * 60 * 60 * 1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(period < 7 && period >= 0) {
            log.info("possibleToChangeNickname 로그 - 1주일 이내의 변경 요청으로 인해 닉네임 변경 불가 => " + period + "일");
            return false;
        } else {
            log.info("possibleToChangeNickname 로그 - 닉네임 변경 가능");
            return true;
        }
    }

    @Transactional
    public void deleteMember() {
        log.info("deleteMember 로그 - 진입");

        Member member = readSession();
        Long memberId = member.getMemberId();
        String email = member.getEmail();

        deleteBoardsAndComments(memberId);

        memberRepo.deleteByMemberId(memberId);
        authRepo.deleteByEmail(email);
    }

    /* 회원탈퇴 시 게시물, 댓글 함께 삭제(상태 변환) */
    private void deleteBoardsAndComments(Long memberId) {
        List<Board> boards = boardRepo.findAllByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));
        List<Comment> comments = commentRepo.findByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 회원입니다."));

        log.info("deleteBoardsAndComments 로그 - 본인 게시물, 댓글 개수 : " + boards.size() + ", " + comments.size());

        for(Board board : boards) {
            board.setBlockStatus(BlockStatus.BLOCKED);
            for(Comment comment : board.getComments()) {
                comment.setBlockStatus(BlockStatus.BLOCKED);
            }
        }
        log.info("deleteBoardsAndComments 로그 - 본인 게시물 및 이하 댓글들 상태 변경 완료");

        for(Comment comment : comments) {
            comment.setBlockStatus(BlockStatus.BLOCKED);
        }
        log.info("deleteBoardsAndComments 로그 - 본인 댓글들 상태 변경 완료");
    }
}
