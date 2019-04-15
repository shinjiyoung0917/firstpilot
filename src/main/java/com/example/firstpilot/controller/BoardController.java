package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.LikeBoard;
import com.example.firstpilot.model.Comment;
import com.example.firstpilot.service.BoardService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class BoardController {
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;

    /* 게시물 (파일 제외) 등록 요청 */
    @PostMapping("/boards")
    public void postBoard(@RequestBody Board board) {
        log.info("postBoard 로그  - 진입");
        log.info("postBoard 로그  - board title : " + board.getTitle());
        this.boardService.createBoard(board);
    }

    /* 로컬 디렉토리(실제 상용화라고 생각하면 서버 디렉토리)에 저장 요청 */
    @PostMapping("/boards/file")
    public String postBoardFile(@RequestParam("uploadFile") MultipartFile uploadFile) {
        log.info("postBoardFile 로그  - 진입");
        String filePath = null;
        try {
            filePath = this.boardService.saveBoardFileInDir(uploadFile);
            log.info("postBoardFile 로그  - filePath : " + filePath);
        } catch(Exception e) {
            log.info("postBoardFile 에러 로그  - " + e);
            e.printStackTrace();
        }
        return filePath;
    }

    /* 게시물 정보 요청 */
    @GetMapping("/boards")
    public Page<Board> getBoardList(@PageableDefault(size = 6, sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return this.boardService.readBoardList(pageable);
    }

    /* 로그인한 회원이 좋아요 누른 게시물 목록 요청 */
    @GetMapping("/boards/likes")
    public List<LikeBoard> getLikeBoardList() { //@RequestParam("memberId") Long memberId
        return this.boardService.readLikeBoardList();
    }

    /* 게시물 좋아요 요청 */
    @PostMapping("/boards/{boardId}/like")
    public void postLikeBoard(@PathVariable("boardId") Long boardId) {
        log.info("postLikeBoard 로그  - 진입");
        this.boardService.createLikeBoard(boardId);
        this.boardService.updateLikeCount(boardId, 0);
    }

    /* 게시물 좋아요 해제 요청 */
    @DeleteMapping("/boards/{boardId}/like")
    public void deleteLikeBoard(@PathVariable("boardId") Long boardId) {
        log.info("deleteLikeBoard 로그  - 진입");
        this.boardService.deleteLikeBoard(boardId);
        this.boardService.updateLikeCount(boardId, 1);
    }

    /* 상세 게시물 정보 요청 */
    @GetMapping("/boards/{boardId}")
    public Board getBoardDetails(@PathVariable("boardId") Long boardId) {
        log.info("getBoardDetails 로그  - 진입");
        return this.boardService.readBoardDetails(boardId);
    }

    /* 게시물 조회수 업데이트 요청 */
    @PutMapping("/boards/{boardId}/hit")
    public void putHitCount(@PathVariable("boardId") Long boardId) {
        log.info("putHitCount 로그  - 진입");
        this.boardService.updateHitCount(boardId);

    }

    /* 게시물 업데이트 요청 */
    @PutMapping("/boards/{boardId}")
    public void putBoard(@PathVariable("boardId") Long boardId, @RequestBody Board boardData) {
        log.info("putBoard 로그  - 진입");
        this.boardService.updateBoard(boardId, boardData);
    }

    /* 게시물 삭제 요청 */
    @DeleteMapping("/boards/{boardId}")
    public void deleteBoard(@PathVariable("boardId") Long boardId) {
        log.info("deleteBoard 로그  - 진입");
        this.boardService.deleteBoard(boardId);
    }

    /* 댓글 등록 요청 */
    @PostMapping("/boards/{boardId}/comments")
    public Comment postComments(@PathVariable("boardId") Long boardId, @RequestBody Comment commentData) {
        log.info("postComments 로그  - 진입");
        this.boardService.updateCommentCount(boardId);
        return this.boardService.createComments(boardId, commentData);
    }

    /* 댓글 정보 요청 */
    @GetMapping("/boards/{boardId}/comments")
    public List<Comment> getComments(@PathVariable("boardId") Long boardId) {
        log.info("getComments 로그  - 진입");
        return this.boardService.readComments(boardId);
    }

    /* 대시보드 (본인이 작성한 글 혹은 댓글) 정보 요청 */
    /*@GetMapping(path = "/members/{nickname}")
    public List<Board> getMyBoard(@PathVariable("nickname") String  nickname) {
        log.info("컨트롤러 로그 - 내가 쓴 게시물 및 댓글 정보 가져오기");

        List<Board> board = new ArrayList<>();
        repo.findAll().forEach(board::add);

        return board;
    }
    */
}
