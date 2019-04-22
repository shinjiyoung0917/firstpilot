package com.example.firstpilot.service;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import org.apache.tomcat.util.json.JSONParser;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

//@Transactional
@Service
public class MemberService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Value("${spring.mail.username}")
    String SENDER_EMAIL;

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

    private boolean lowerCheck;
    private int size;

    private CurrentTime currentTime = new CurrentTime();

    /* 로그인 인증 (시큐리티 내부에서 자동 호출?) */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 로그 - email : " + username);

        String encodedEmail = encryptSHA256(username);
        Member member = memberRepo.findByEmail(encodedEmail);
        if(member == null) {
            throw new UsernameNotFoundException(username);
        }

        //List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        //User user = new User(member.getEmail(), member.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        return new LoginUserDetails(member);
    }

    /* SHA256 해시 함수 (이메일을 위한 것) */
    public String encryptSHA256(String email) {
        String SHA = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256"); // 이 부분을 SHA-1으로 바꿔도 된다!
            messageDigest.update(email.getBytes());
            byte byteData[] = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                stringBuffer.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return SHA;
    }

    /* 인증코드 생성 */
    public String getKey(int size, boolean lowerCheck) {
        this.size = size;
        this.lowerCheck = lowerCheck;

        Random randoms = new Random();
        StringBuffer strBuff = new StringBuffer();
        int num = 0;
        do {
            num = randoms.nextInt(75)+48;
            if((num>=48 && num<=57) || (num>=65 && num<=90) || (num>=97 && num<=122)) {
                strBuff.append((char)num);
            }else {
                continue;
            }
        } while (strBuff.length() < size);
        if(lowerCheck) {
            return strBuff.toString().toLowerCase();
        }
        return strBuff.toString();
    }

    /* 이메일 인증코드 삽입 */
    public MailAuth createAuthKey(MailAuth mailAuthData) throws MessagingException {
        log.info("createAuthKey 로그 - 진입");

        // 이메일 중복 검사
        String email = mailAuthData.getEmail();
        String encodedEmail = encryptSHA256(email);
        Member isMailExist = this.memberRepo.findByEmail(encodedEmail);
        if(isMailExist == null) {
            String key = getKey(50, false);
            StringBuffer text = new StringBuffer().append("회원가입 인증코드입니다.\n")
                    .append(key);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(SENDER_EMAIL);
            messageHelper.setTo(email);
            message.setSubject("[익명게시판] 회원가입용 인증 코드 발송");
            message.setText(text.toString());
            message.setSentDate(new Date());
            mailSender.send(message);

            MailAuth mailAuth = new MailAuth();
            mailAuth.setEmail(encodedEmail);
            mailAuth.setAuthType(1);
            mailAuth.setAuthKey(key);
            String currentTimeString = this.currentTime.getCurrentTime();
            mailAuth.setCreatedDate(currentTimeString);
            return this.authRepo.save(mailAuth);
        } else {
            mailAuthData.setEmail("Already-Exist"); // 바꾸기
            return mailAuthData;
        }
    }

    /* 회원가입 */
    public Member createMember(Member memberData) {
        log.info("createMember 로그 - 진입");

        String email = memberData.getEmail();
        String password = memberData.getPassword();
        String encodedEmail = encryptSHA256(email);
        String encodedPassword = passwordEncoder.encode(password);

        // 이메일 중복 검사
        Member isMailExist = this.memberRepo.findByEmail(encodedEmail);
        if(isMailExist == null) {
            // 임시 닉네임 생성 및 중복 검사
            Random randoms = new Random();
            StringBuffer strBuff = new StringBuffer();
            String nickname = "";
            Member isNicknameExist = null;
            do {
                for (int i = 0; i < 10; i++) {
                    int rIndex = randoms.nextInt(3);
                    switch (rIndex) {
                        case 0:
                            // A-Z
                            strBuff.append((char) ((int) (randoms.nextInt(26)) + 65));
                            break;
                        case 1:
                            // 0-9
                            strBuff.append((randoms.nextInt(10)));
                            break;
                    }
                }
                nickname = strBuff.toString();
                isNicknameExist = this.memberRepo.findByNickname(nickname);
            } while (isNicknameExist != null);

            // 정규식 검사
            String emailDxpText = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
            String passwordDxpText = "^[A-Za-z0-9]{6,12}$";

            if (Pattern.matches(emailDxpText, email)) {
                log.info("signUp 로그 - 올바른 이메일 패턴");
                if (Pattern.matches(passwordDxpText, password)) {
                    log.info("signUp 로그 - 올바른 비밀번호 패턴");

                    Member member = new Member();
                    member.setEmail(encodedEmail);
                    member.setPassword(encodedPassword);
                    member.setNickname(nickname);
                    MemberRole role = new MemberRole();
                    //role.setRoleName("ROLE_USER");
                    member.setRole("ROLE_USER"); //Arrays.asList(role)
                    return this.memberRepo.save(member);
                } else {
                    log.info("signUp 로그 - 잘못된 비밀번호 패턴");
                    return null;
                }
            } else {
                log.info("signUp 로그 - 잘못된 이메일 패턴");
                return null;
            }
        } else {
            // throw로 바꾸기
            //memberData.setEmail("Already-Exist");
            return memberData;
        }
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
    public Member updateMember(Member memberData) throws ParseException {
        log.info("updateMember 로그 - 진입");

        // 닉네임 중복 불가
        Member isNicknameExist = this.memberRepo.findByNickname(memberData.getNickname());
        if(isNicknameExist != null) {
            log.info("updateMember 로그 - 중복으로 인해 닉네임 변경 불가");
            return null; // throw로 바꾸기
        }
        log.info("updateMember 로그 - 닉네임 중복 아님");

        // 1주일 이내에 변경했는지 검사 (이전에 변경 후 1주일 이내 변경 불가)
        String currentTimeString = this.currentTime.getCurrentTime();
        log.info("updateMember 로그 - 지금 시간(String) : " + currentTimeString);
        SimpleDateFormat dateFormat = this.currentTime.getDateFormat();
        Date current = dateFormat.parse(currentTimeString);
        log.info("updateMember 로그 - 지금 시간(Date) : " + current);
        Date past;
        if(memberData.getUpdatedDate() != null) {
            past = dateFormat.parse(memberData.getUpdatedDate());
            log.info("updateMember 로그 - 이전에 업데이트 했던 시간(Date) : " + past);

            long diffDay = (current.getTime() - past.getTime()) / (24 * 60 * 60 * 1000);
            if(diffDay < 7) {
                log.info("updateMember 로그 - 1주일 이내의 변경 요청으로 인해 닉네임 변경 불가 => " + diffDay + "일");
                return null; // throw로 바꾸기
            }
        }

        log.info("updateMember 로그 - 닉네임 변경 가능");
        Member member = readSession();
        member.setNickname(member.getNickname());
        member.setUpdatedDate(currentTimeString);
        this.memberRepo.save(member);
        return member;
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
