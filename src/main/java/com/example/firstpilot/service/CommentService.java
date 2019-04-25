package com.example.firstpilot.service;

import com.example.firstpilot.util.BlockStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Comment;
import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.Member;
import com.example.firstpilot.repository.CommentRepository;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.util.CurrentTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;

import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private MemberService memberService;

    private CurrentTime currentTime = new CurrentTime();

    /* 댓글 정보 삽입 */
    public Comment createOrUpdateComments(Long boardId, Long commentId, Comment commentData) {
        log.info("createOrUpdateComments 로그  - 진입");
        log.info("createOrUpdateComments 로그  - filePath : " + commentData.getFilePath());

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED);
        Member member = memberService.readSession();

        Comment comment;
        if(commentId == null) {         // 댓글 등록
            comment = new Comment();
        } else {                        // 댓글 업데이트
            comment = commentRepo.findByCommentId(commentId);
        }
        comment.setContent(commentData.getContent());
        comment.setBoard(board);
        comment.setMember(member);
        comment.setMemberId(member.getMemberId());
        if(commentId == null) {         // 댓글 등록
            comment.setNickname(member.getNickname());
        } else {                        // 댓글 업데이트
            comment.setNickname(commentData.getNickname());
        }
        String currentTimeString = this.currentTime.getCurrentTime();
        if(commentId == null) {         // 댓글 등록

            comment.setCreatedDate(currentTimeString);
        } else {                        // 댓글 업데이트
            comment.setUpdatedDate(currentTimeString);
        }
        comment.setParentId(commentData.getParentId());
        comment.setChildCount((long)0);
        if(commentData.getFilePath() == null || commentData.getFilePath().equals("")) {
            log.info("createOrUpdateComments 로그  - 파일을 선택하지 않음");
            comment.setFilePath(null);
        } else {
            log.info("createOrUpdateComments 로그  - 파일 선택함");
            comment.setFilePath(commentData.getFilePath().substring(6));
        }
        comment.setBlockStatus(BlockStatus.UNBLOCKED);

        try {
            return this.commentRepo.save(comment);
        } catch(DataAccessException e) {
            throw new Error("Comment Insert Error");
        }
    }

    /* 댓글 삭제 */
    public void deleteComment(Long commentId) {
        log.info("deleteComment 로그  - 진입");
        Comment comment = this.commentRepo.findByCommentId(commentId);
        comment.getBoard().setCommentCount(comment.getBoard().getCommentCount() - 1);
        comment.setBlockStatus(BlockStatus.BLOCKED);
        this.commentRepo.save(comment);
    }

    /* 대시보드 댓글 */
    public List<Comment> readMyComments() {
        log.info("readMyComments 로그  - 진입");
        Long memberId = memberService.readSession().getMemberId();
        return this.commentRepo.findByMemberIdAndBlockStatus(memberId, BlockStatus.UNBLOCKED);
    }
}
