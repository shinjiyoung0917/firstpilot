package com.example.firstpilot.service;

import com.example.firstpilot.exceptionAndHandler.NotFoundMemberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Comment;
import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.Member;
import com.example.firstpilot.dto.CommentDto;
import com.example.firstpilot.repository.CommentRepository;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.util.BlockStatus;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import com.example.firstpilot.exceptionAndHandler.NotFoundCommentException;
import com.example.firstpilot.exceptionAndHandler.NotFoundBoardException;

import javax.transaction.Transactional;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private MemberService memberService;

    private Integer PREFIX_THUMB_LENGTH = "thumb_".length();

    /* 댓글 정보 삽입 */
    @Transactional
    public Comment createComments(Long boardId, CommentDto commentDto) {
        log.info("createOrUpdateComments 로그  - 진입");
        log.info("createOrUpdateComments 로그  - filePath : " + commentDto.getFilePath());

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        Member member = memberService.readSession();

        Comment comment = commentDto.toEntity(board, member);

        String nullableFilePath = commentDto.getFilePath();
        Optional.ofNullable(nullableFilePath)
                .ifPresent(value -> {
                    comment.setFilePath(nullableFilePath.substring(PREFIX_THUMB_LENGTH));
                });

        board.increaseCommentCount();
        boardRepo.save(board);
        return commentRepo.save(comment);
    }

    public void updateComments(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepo.findByCommentIdAndBlockStatus(commentId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundCommentException());

        commentRepo.save(comment.updateCommentEntity(commentDto));
    }

    public void deleteComment(Long commentId) {
        log.info("deleteComment 로그  - 진입");
        Comment comment = commentRepo.findByCommentIdAndBlockStatus(commentId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundCommentException());
        comment.getBoard().decreaseCommentCount();
        comment.updateCommentBlockStatus();
        commentRepo.save(comment);
    }

    public List<Comment> readMyComments() {
        log.info("readMyComments 로그  - 진입");
        Long memberId = memberService.readSession().getMemberId();
        List<Comment> myCommentList = commentRepo.findByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundMemberException());
        return myCommentList;
    }
}
