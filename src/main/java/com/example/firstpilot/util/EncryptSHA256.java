package com.example.firstpilot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptSHA256 {
    /* SHA256 해시 함수 (이메일을 위한 것) */
    public String encryptSHA256(String email) {
        String sha = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(email.getBytes());
            byte byteData[] = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; ++i){
                stringBuffer.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            sha = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return sha;
    }
}
