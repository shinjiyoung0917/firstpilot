package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    @Getter @Setter
    private String userID;

    @Column(name = "email")
    @Getter @Setter
    private String email;

    @Column(name = "password")
    @Getter @Setter
    private String password;

    @Column(name = "nickname")
    @Getter @Setter
    private String nickname;

    @Column(name = "nickname_change_date")
    @Getter @Setter
    private String nicknameChange_date;
}


