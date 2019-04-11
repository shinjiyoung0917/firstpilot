package com.example.firstpilot.controller;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.service.BoardService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BoardController {
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;
    //@Autowired
    //BoardRepository boardRepo;

    /* 게시물 정보 삽입하기 */
    @PostMapping("/boards")
    public void postBoard(@RequestBody Board board) {
        this.boardService.createBoard(board);
    }



    /* 대시보드 (본인이 작성한 글 혹은 댓글) */
    /*@GetMapping(path = "/members/{nickname}")
    public List<Board> getMyBoard(@PathVariable("nickname") String  nickname) {
        log.info("컨트롤러 로그 - 내가 쓴 게시물 및 댓글 정보 가져오기");

        List<Board> board = new ArrayList<>();
        repo.findAll().forEach(board::add);

        return board;
    }
    */
}
