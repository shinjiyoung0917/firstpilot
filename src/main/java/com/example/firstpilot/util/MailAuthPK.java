package com.example.firstpilot.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailAuthPK implements Serializable {
    private String email;
    private AuthType authType;
}
