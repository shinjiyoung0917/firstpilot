package com.example.firstpilot.controller;

import com.example.firstpilot.model.User;
import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.repository.UserRepository;
import com.example.firstpilot.service.UserService;
//import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository repo;
    @Autowired
    private UserService userService;

    /* 이메일 인증코드 생성하기 */
    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public void postAuthKey(@RequestBody MailAuth mailAuth) { //String userEmail
        log.info("postAuthKey 로그 - 진입");
        log.info("postAuthKey 로그 - data : " + mailAuth.getEmail());
        userService.createAuthKey(mailAuth);
    }

    /* 이메일 인증코드 가져오기 */
    @GetMapping("/auth")
    public List<MailAuth> getAuthKey(@RequestBody MailAuth mailAuth) { //String userEmail
        log.info("getAuthKey 로그 - 진입");
        log.info("getAuthKey 로그 - data1 : " + mailAuth.getEmail());
        log.info("getAuthKey 로그 - data2 : " + mailAuth.getAuthType());
        return userService.readAuthKey(mailAuth);
    }

    /* 사용자 정보 삽입하기 */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@RequestBody User user) {
        log.info("postUser 로그 - 진입");
        log.info("postUser 로그 - data1 : " + user.getEmail());
        log.info("postUser 로그 - data2 : " + user.getPassword());
        return userService.createUser(user);
    }

    /* 사용자 정보 가져오기 */
    @GetMapping("/users")
    public List<User> getUser() {
        log.info("getUser 로그 - 진입");

        List<User> users = new ArrayList<>();
        repo.findAll().forEach(users::add);

        return users;
    }

}

