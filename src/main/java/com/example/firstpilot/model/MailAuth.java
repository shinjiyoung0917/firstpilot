package com.example.firstpilot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(MailAuthPK.class)
@Table(name = "mail_auth")
public class MailAuth {
    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Id
    @Column(name = "auth_type", nullable = false)
    private Integer authType;

    @Column(name = "auth_key", nullable = false)
    private String authKey;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
