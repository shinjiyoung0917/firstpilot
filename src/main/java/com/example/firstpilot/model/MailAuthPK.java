package com.example.firstpilot.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailAuthPK implements Serializable {
    private String email;
    private Integer authType;
}
