package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuthMail {
    @Id
    @GeneratedValue
    @Column(name = "auth_mail_id")
    @Getter
    private Long authMailID;

    @Column(name = "email", nullable = false)
    @Getter @Setter
    private String email;

    @Column(name = "auth_type", nullable = false)
    @Getter @Setter
    private Integer authType;

    @Column(name = "auth_key", nullable = false)
    @Getter @Setter
    private String authKey;

    @Column(name = "created_date", nullable = false)
    @Getter @Setter
    private LocalDateTime createdDate;
}
