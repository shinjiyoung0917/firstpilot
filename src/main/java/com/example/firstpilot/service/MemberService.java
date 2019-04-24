package com.example.firstpilot.service;

import com.example.firstpilot.dto.MailAuthDto;
import com.example.firstpilot.dto.MemberDto;
import com.example.firstpilot.exceptionAndHandler.AlreadyExistedNicknameException;
import com.example.firstpilot.exceptionAndHandler.NotTimeForNicknameChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.*;
import com.example.firstpilot.repository.MemberRepository;
import com.example.firstpilot.repository.MailAuthRepository;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.repository.CommentRepository;
import com.example.firstpilot.util.LoginUserDetails;
import com.example.firstpilot.util.CurrentTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import com.example.firstpilot.exceptionAndHandler.AlreadyExistedEmailException;

//@Transactional
@Service
public class MemberService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Value("${spring.mail.username}")
    String SENDER_EMAIL;

    Integer KEY_SIZE = 50;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private MailAuthRepository authRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private CommentRepository commentRepo;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // TODO: 지우기
    private CurrentTime currentTime = new CurrentTime();

    /* 이메일 인증코드 삽입 */
    public MailAuth createAuthKey(MailAuthDto mailAuthDto) {
        log.info("createAuthKey 로그 - 진입");

        MailAuth mailAuth = mailAuthDto.toEntity();

        // TODO: 이메일 NULL일 경우 처리
        String encryptEmail = mailAuth.encryptEmail();

        if(!duplicatedEmail(encryptEmail)) {
            String authKey = mailAuth.generateKey(KEY_SIZE, false);
            String email = mailAuthDto.getEmail();
            sendMail(authKey, email);

            mailAuth.setEmail(encryptEmail);
            mailAuth.setAuthKey(authKey);
           return this.authRepo.save(mailAuth);
        } else {
            throw new AlreadyExistedEmailException();
        }
    }

    /* 이메일 중복 검사 */
    public boolean duplicatedEmail(String encryptEmail) {
        if(memberRepo.findByEmail(encryptEmail).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    /* 메일 발송 */
    public void sendMail(String key, String email) {
        StringBuffer text = new StringBuffer().append("회원가입 인증코드입니다.\n").append(key);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(SENDER_EMAIL);
            messageHelper.setTo(email);
            message.setSubject("[익명게시판] 회원가입용 인증 코드 발송");
            message.setText(text.toString());
            message.setSentDate(new Date());
            mailSender.send(message);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }

    /* 회원가입 */
    public Member createMember(MemberDto memberDto) {
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

            member.setEmail(encryptEmail);
            member.setPassword(encryptPassword);
            member.setNickname(nickname);
            member.setRole("ROLE_USER");
            return this.memberRepo.save(member);
        } else {
            throw new AlreadyExistedEmailException();
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

    /* 로그인 인증 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 로그 - email : " + username);

        Member member = null;
        String encryptEmail = member.encryptSHA256(username);
        member = memberRepo.findByEmail(encryptEmail)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new LoginUserDetails(member);
    }

    /* 로그인한 회원의 세션 값 가져오기 */
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
            Member member = this.memberRepo.findByMemberId(memberId);
            return member;
        }
    }

    /* 닉네임 변경 */
    public Member updateMember(MemberDto memberDto) throws ParseException {
        log.info("updateMember 로그 - 진입");

        // 닉네임 중복 불가
        if(!duplicatedNickname(memberDto.getNickname())) {
            log.info("updateMember 로그 - 닉네임 중복 아님");

        } else {
            log.info("updateMember 로그 - 중복으로 인해 닉네임 변경 불가");
            throw new AlreadyExistedNicknameException();
        }

        long period = computePeriodOfAfterNicknameChange(memberDto.getUpdatedDate());
        if(period < 7 && period >= 0) {
            log.info("updateMember 로그 - 1주일 이내의 변경 요청으로 인해 닉네임 변경 불가 => " + period + "일");
            throw new NotTimeForNicknameChange();
        }

        log.info("updateMember 로그 - 닉네임 변경 가능");
        return memberRepo.save(member.update())

        Member member = readSession();
        member.setNickname(memberDto.getNickname());
        member.setUpdatedDate(currentTimeString);
        this.memberRepo.save(member);
        return member;
    }

    public long computePeriodOfAfterNicknameChange(String updatedDate) {
        long period = -1;

        String currentTimeString = this.currentTime.getCurrentTime();
        log.info("updateMember 로그 - 지금 시간(String) : " + currentTimeString);

        try {
            SimpleDateFormat dateFormat = this.currentTime.getDateFormat();
            Date current = dateFormat.parse(currentTimeString);
            log.info("updateMember 로그 - 지금 시간(Date) : " + current);

            Date past;
            // TODO: 변경날짜 null일 경우 처리
            if(updatedDate != null) {
                past = dateFormat.parse(updatedDate);
                log.info("updateMember 로그 - 이전에 업데이트 했던 시간(Date) : " + past);

                period = (current.getTime() - past.getTime()) / (24 * 60 * 60 * 1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return period;
    }

    /* 회원탈퇴 */
    @Transactional
    public void deleteMember() {
        log.info("deleteMember 로그 - 진입");
        Member member = readSession();
        Long memberId = member.getMemberId();
        String email = member.getEmail();

        List<Board> boards = this.boardRepo.findAllByMemberIdAndUnblocked(memberId, 1);
        List<Comment> comments = this.commentRepo.findByMemberIdAndUnblocked(memberId, 1);

        log.info("deleteMember 로그 - 본인 게시물 개수 : " + boards.size());
        log.info("deleteMember 로그 - 본인 댓글 개수 : " + comments.size());

        for(Board board : boards) {
            board.setUnblocked(0);
            for(Comment comment : board.getComments()) {
                comment.setUnblocked(0);
            }
        }
        log.info("deleteMember 로그 - 본인 게시물 및 이하 댓글들 상태 변경 완료");
        for(Comment comment : comments) {
            comment.setUnblocked(0);
        }
        log.info("deleteMember 로그 - 본인 댓글들 상태 변경 완료");

        this.memberRepo.deleteByMemberId(memberId);
        this.authRepo.deleteByEmail(email);
    }
}
