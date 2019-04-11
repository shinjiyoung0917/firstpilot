package com.example.firstpilot.service;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.repository.BoardRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepo;

    public void createBoard(Board board) {
        // 제목
        // 내용
        // 작성자 시퀀스
        // 작성자 닉네임
        // 작성날짜
        // 첨부파일 경로

    }

}
