package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.firstpilot.model.User;
import com.example.firstpilot.repository.UserRepository;
import com.example.firstpilot.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository repo;
    private UserService userService;

    /* 사용자 정보 삽입하기 */
    @PostMapping(path = "/users")
    public void postUser(@RequestBody User user) {
        log.info("postUser 로그 - 진입");
        log.info("postUser 로그 - data : " + user.toString());
        log.info("postUser 로그 - data2 : " + user.getEmail());
        userService.signUp(user);
    }

    /* 사용자 정보 가져오기 */
    @GetMapping(path = "/users")
    public List<User> getUser() {
        log.info("getUser 로그 - 사용자 정보 가져오기");

        List<User> users = new ArrayList<>();
        repo.findAll().forEach(users::add);

        return users;
    }

}

