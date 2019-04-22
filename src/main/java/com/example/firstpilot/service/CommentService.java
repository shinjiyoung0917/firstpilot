package com.example.firstpilot.service;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.Member;
import com.example.firstpilot.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Comment;
import com.example.firstpilot.repository.CommentRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
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

    /* 댓글 정보 삽입 */
    public Comment createOrUpdateComments(Long boardId, Long commentId, Comment commentData) {
        log.info("createComments 로그  - 진입");
        log.info("createComments 로그  - filePath : " + commentData.getFilePath());

        Board board = boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
        Member member = memberService.readSession();

        Comment comment;
        if(commentId == null) {         // 댓글 등록
            comment = new Comment();
            comment.setBoard(board);
            //comment.setMember(member);
        } else {                        // 댓글 업데이트
            comment = commentRepo.findByCommentId(commentId);
        }
        //comment.setBoardId(boardId);
        comment.setContent(commentData.getContent());
        comment.setMemberId(member.getMemberId());
        comment.setNickname(commentData.getNickname());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setParentId(commentData.getParentId());
        comment.setChildCount((long)0);
        if(commentData.getFilePath().equals("") || commentData.getFilePath() == null) {
            log.info("createComments 로그  - 파일을 선택하지 않음");
            comment.setFilePath(null);
        } else {
            log.info("createComments 로그  - 파일 선택함");
            comment.setFilePath(commentData.getFilePath().substring(6));
        }
        comment.setIsNotBlocked(1);


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
        comment.setIsNotBlocked(0);        // 블라인드 되도록 유효상태 변경
        this.commentRepo.save(comment);
    }

    /* // 댓글 정보 가져오기
    public List<Comment> readComments(Long boardId) {
        log.info("readComments 로그  - 진입");
        return this.commentRepo.findByBoardId(boardId, new Sort(Sort.Direction.DESC, "createdDate"));
    }
    */

    /* // 댓글 업데이트
    public void updateComment(Long boardId, Long commentId, Comment commentData) {
        log.info("updateComment 로그  - 진입");
        Comment comment = commentRepo.findByCommentId(commentId);
        comment.setContent(commentData.getContent());
        comment.setUpdatedDate(LocalDateTime.now() );
        if(commentData.getFilePath().equals("") || commentData.getFilePath() == null) {
            comment.setFilePath(null);
        } else {
            comment.setFilePath(commentData.getFilePath());
        }
        comment.setIsValid(commentData.getIsValid());
        this.commentRepo.save(comment);
    }
    */
}
