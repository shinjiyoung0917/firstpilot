package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Comment;
import com.example.firstpilot.dto.CommentDto;
import com.example.firstpilot.service.CommentService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

import java.util.List;

@RestController
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @PostMapping("/boards/{boardId}/comments")
    public Comment postComments(@PathVariable("boardId") Long boardId, @RequestBody @Valid CommentDto commentDto) {
        log.info("postComments 로그  - 진입");

        return commentService.createComments(boardId, commentDto);
    }

    @PutMapping("/boards/{boardId}/comments/{commentId}")
    public void putComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, @RequestBody @Valid CommentDto commentDto) {
        log.info("putComment 로그  - 진입");

        commentService.updateComments(commentId, commentDto);
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    public void deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId) {
        log.info("deleteComment 로그  - 진입");

        commentService.deleteComment(commentId);
    }

    @GetMapping("/dashboards/comments")
    public List<Comment> getMyComments() {
        log.info("getMyBoard 로그 - 진입");

        return commentService.readMyComments();
    }
}
