package com.example.firstpilot.service;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.repository.BoardRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;


@Service
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private BoardRepository boardRepo;

    /* 게시물 정보 */
    public void createBoard(Board boardData) {
        log.info("createBoard 로그  - 진입");

        Board board = new Board();
        board.setTitle(boardData.getTitle());
        board.setContent(boardData.getContent());
        board.setMemberId(boardData.getMemberId());
        board.setNickname(boardData.getNickname());
        board.setCreatedDate(LocalDateTime.now());
        board.setFilePath("");  // 첨부파일 경로
        //this.boardRepo.save(board);
    }

    public void createBoardFile() {

    }

}
