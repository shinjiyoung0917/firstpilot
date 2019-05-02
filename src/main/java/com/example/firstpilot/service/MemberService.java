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

import com.example.firstpilot.exceptionAndHandler.AlreadyExistedEmailException;
import com.example.firstpilot.exceptionAndHandler.AlreadyExistedNicknameException;
import com.example.firstpilot.exceptionAndHandler.NotFoundMemberException;
import com.example.firstpilot.exceptionAndHandler.NotTimeForNicknameChange;

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

    public MemberDto createMember(MemberDto memberDto) {
        log.info("createMember 로그 - 진입");

        Member member = memberDto.toEntity();

        String encryptEmail = member.encryptEmail();
        String encryptPassword = member.encryptPassword(passwordEncoder);

        String nickname = "";
        if(!duplicatedEmail(encryptEmail)) {
            boolean isNicknameExist = true;

            do {
                nickname = member.generateTemporaryNickname();
                if(!duplicatedNickname(nickname)) {
                    isNicknameExist = false;
                }
            } while (isNicknameExist);

            // TODO: 이 방법도 괜찮은지?
            member = Member.builder()
                    .email(encryptEmail)
                    .nickname(nickname)
                    .password(encryptPassword)
                    .role("ROLE_USER")
                    .build();
            return memberRepo.save(member).toDto();
        } else {
            throw new AlreadyExistedEmailException();
        }
    }

    public boolean duplicatedEmail(String encryptEmail) {
        log.info("duplicatedEmail 로그 - 진입");
        return memberRepo.findByEmail(encryptEmail).isPresent();
    }

    private boolean duplicatedNickname(String nickname) {
        log.info("duplicatedNickname 로그 - 진입");
        return memberRepo.findByNickname(nickname).isPresent();
    }

    /* 로그인(security 인증) */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 로그 - email : " + username);

        Member member = Member.builder().build();
        String encryptEmail = member.encryptSHA256(username);
        member = memberRepo.findByEmail(encryptEmail)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new LoginUserDetails(member);
    }

    // TODO: MemberDto로 해야할지 고민해보자
    public Member readSession() {
        log.info("readSession 로그 - 진입");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        } else {
            LoginUserDetails login = (LoginUserDetails) authentication.getPrincipal();
            log.info("readSession 로그 - member : " + login);
            log.info("readSession 로그 - member id : " + login.getMemberId());
            Long memberId = login.getMemberId();
            Member member = memberRepo.findByMemberId(memberId)
                    .orElseThrow(() -> new NotFoundMemberException());
            return member;
        }
    }

    /* 닉네임 변경 가능 여부 (단순히 닉네임 수정 버튼 눌렀을 때) */
    public Boolean readMemberNicknameChangePeriod(Long memberId) {
        Member member = memberRepo.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
        return possibleToChangeNickname(member.getUpdatedDate());
    }

    public MemberDto updateMember(MemberDto memberDto) {
        log.info("updateMember 로그 - 진입");

        String beforeChangingNickname = readSession().getNickname();
        Member member = memberRepo.findByNickname(beforeChangingNickname)
                .orElseThrow(() -> new NotFoundMemberException());

        if(duplicatedNickname(memberDto.getNickname())) {
            log.info("updateMember 로그 - 중복으로 인해 닉네임 변경 불가");
            throw new AlreadyExistedNicknameException();
        }

        log.info("updateMember 로그 - 닉네임 중복 아님");
        if(possibleToChangeNickname(memberDto.getUpdatedDate())) {
            return memberRepo.save(member.updateMemberEntity(memberDto)).toDto();
        } else {
            throw new NotTimeForNicknameChange();
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
                .orElseThrow(() -> new NotFoundMemberException());
        List<Comment> comments = commentRepo.findByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundMemberException());

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
