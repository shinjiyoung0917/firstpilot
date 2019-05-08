package com.example.firstpilot.service;

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

import javax.transaction.Transactional;

import com.example.firstpilot.exceptionAndHandler.NotFoundResourcesException;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private MemberService memberService;

    /* 댓글 정보 삽입 */
    @Transactional
    public Comment createComments(Long boardId, CommentDto commentDto) {
        log.info("createOrUpdateComments 로그  - 진입");
        log.info("createOrUpdateComments 로그  - filePath : " + commentDto.getFilePath());

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));
        Member member = memberService.readSession();

        Comment comment = commentDto.toEntity(board, member);
        if(comment.getParentId() != null) {
            comment.increaseChildCount();
        }
        board.increaseCommentCount();
        boardRepo.save(board);
        return commentRepo.save(comment);
    }

    public void updateComments(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepo.findByCommentIdAndBlockStatus(commentId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 댓글입니다."));

        commentRepo.save(comment.updateCommentEntity(commentDto));
    }

    public void deleteComment(Long commentId) {
        log.info("deleteComment 로그  - 진입");

        Comment comment = commentRepo.findByCommentIdAndBlockStatus(commentId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 댓글입니다."));

        if(comment.getParentId() != null) {
            comment.decreaseChildCount();
        }
        comment.getBoard().decreaseCommentCount();
        comment.updateCommentContentAndBlockStatus();
        commentRepo.save(comment);
    }

    public List<Comment> readMyComments() {
        log.info("readMyComments 로그  - 진입");

        Long memberId = memberService.readSession().getMemberId();
        List<Comment> myCommentList = commentRepo.findByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 회원입니다."));
        return myCommentList;
    }
}
