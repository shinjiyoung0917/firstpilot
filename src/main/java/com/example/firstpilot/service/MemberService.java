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

//@Transactional
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


    /* 회원가입 */
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

            // TODO: set
            member.setEmail(encryptEmail);
            member.setPassword(encryptPassword);
            member.setNickname(nickname);
            member.setRole("ROLE_USER");
            return memberRepo.save(member).toDto();
        } else {
            throw new AlreadyExistedEmailException();
        }
    }

    /* 이메일 중복 검사 */
    public boolean duplicatedEmail(String encryptEmail) {
        log.info("duplicatedEmail 로그 - 진입");
        if(memberRepo.findByEmail(encryptEmail).isPresent()) {
            log.info("duplicatedEmail 로그 - 이메일 존재");
            return true;
        } else {
            log.info("duplicatedEmail 로그 - 이메일 존재하지 않음");
            return false;
        }
    }

    /* 닉네임 중복 검사 */
    public boolean duplicatedNickname(String nickname) {
        if(memberRepo.findByNickname(nickname).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    /* 로그인(security 인증) */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 로그 - email : " + username);

        Member member = new Member(null, null, null, null);
        String encryptEmail = member.encryptSHA256(username);
        member = memberRepo.findByEmail(encryptEmail)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new LoginUserDetails(member);
    }

    /* 세션 값 가져오기 */
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

    /* 닉네임 변경 가능 여부 */
    public Boolean readMemberNicknameChangePeriod(Long memberId) {
        Member member = memberRepo.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
        return possibleToChangeNickname(member.getUpdatedDate());
    }

    /* 닉네임 변경 */
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

    /* 닉네임 변경가능한지의 여부 판단 */
    public boolean possibleToChangeNickname(String updatedDate) {
        long period = -1;

        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        log.info("possibleToChangeNickname 로그 - 지금 시간(String) : " + currentTimeString);

        try {
            SimpleDateFormat dateFormat = currentTime.getDateFormat();
            Date current = dateFormat.parse(currentTimeString);
            log.info("possibleToChangeNickname 로그 - 지금 시간(Date) : " + current);

            // TODO: 변경날짜 null일 경우 처리
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

    /* 회원탈퇴 */
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
    public void deleteBoardsAndComments(Long memberId) {
        // TODO: Board, Comment도 Optional로 바꿔서 익셉션 처리하기
        List<Board> boards = boardRepo.findAllByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED);
        List<Comment> comments = commentRepo.findByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED);

        log.info("deleteBoardsAndComments 로그 - 본인 게시물 개수 : " + boards.size());
        log.info("deleteBoardsAndComments 로그 - 본인 댓글 개수 : " + comments.size());

        // TODO: Board, Comment도 setBlockStatus말고 update 메서드로 변경하기
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
