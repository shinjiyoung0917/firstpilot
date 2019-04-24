package com.example.firstpilot.exceptionAndHandler;

public class NotTimeForNicknameChange extends RuntimeException {
    public NotTimeForNicknameChange() { super("닉네임을 변경할 수 있는 기간이 아닙니다."); }
}
