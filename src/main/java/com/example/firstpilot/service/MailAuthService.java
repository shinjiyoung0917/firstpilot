package com.example.firstpilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.dto.MailAuthDto;
import com.example.firstpilot.repository.MailAuthRepository;
import com.example.firstpilot.util.AuthType;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.Date;

import com.example.firstpilot.exceptionAndHandler.AlreadyExistedEmailException;

@Service
public class MailAuthService {
    private static final Logger log = LoggerFactory.getLogger(MailAuthService.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private MailAuthRepository authRepo;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String SENDER_EMAIL;

    Integer KEY_SIZE = 50;

    public MailAuthDto createAuthKey(MailAuthDto mailAuthDto) {
        log.info("createAuthKey 로그 - 진입");

        MailAuth mailAuth = mailAuthDto.toEntity();

        String encryptedEmail = mailAuth.encryptEmail();
        log.info("createAuthKey 로그 - encryptedEmail : " + encryptedEmail);

        if(!memberService.checkDuplicatedEmail(encryptedEmail)) {
            log.info("createAuthKey 로그 - 중복된 이메일 아님");

            String authKey = mailAuth.generateKey(KEY_SIZE, false);
            String email = mailAuthDto.getEmail();
            sendMail(authKey, email);

            mailAuth = MailAuth.builder()
                    .email(encryptedEmail)
                    .authType(AuthType.SIGN_UP)
                    .authKey(authKey)
                    .build();

            return authRepo.save(mailAuth).toDto();
        } else {
            throw new AlreadyExistedEmailException();
        }
    }

    public void sendMail(String key, String email) {
        log.info("sendMail 로그 - 진입");
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

}
