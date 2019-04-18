package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Comment;
import com.example.firstpilot.service.CommentService;
import com.example.firstpilot.service.BoardService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;

    /* 댓글 등록 및 업데이트 요청 */
    @PostMapping("/boards/{boardId}/comments")
    public Comment postComments(@PathVariable("boardId") Long boardId, @RequestBody Comment commentData) {
        log.info("postComments 로그  - 진입");
        this.boardService.updateCommentCount(boardId);
        return this.commentService.createOrUpdateComments(boardId, null, commentData);
    }

    /* 댓글 업데이트 요청 */
    @PutMapping("/boards/{boardId}/comments/{commentId}")
    public void putComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, @RequestBody Comment commentData) {
        log.info("putComment 로그  - 진입");
        this.commentService.createOrUpdateComments(boardId, commentId, commentData);
    }

    /* 댓글 삭제 요청 */
    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    public void deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId) {
        log.info("deleteComment 로그  - 진입");
        this.boardService.updateCommentCount(boardId);
        this.commentService.deleteComment(commentId);
    }

    /* 대시보드 (본인이 작성한 글) 정보 요청 */
    @GetMapping("/dashboards/comments")
    public List<Comment> getMyComments() {
        log.info("getMyBoard 로그 - 진입");
        return this.commentService.readMyComments();
    }
}