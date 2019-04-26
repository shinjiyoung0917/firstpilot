package com.example.firstpilot.service;

import com.example.firstpilot.exceptionAndHandler.NotFoundBoardException;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.LikeBoard;
import com.example.firstpilot.model.Comment;
import com.example.firstpilot.model.Member;
import com.example.firstpilot.dto.BoardDto;
import com.example.firstpilot.repository.MemberRepository;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.repository.LikeBoardRepository;
import com.example.firstpilot.util.LikeBoardPK;
import com.example.firstpilot.util.BlockStatus;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import com.example.firstpilot.exceptionAndHandler.NotFoundMemberException;

import javax.transaction.Transactional;

@Service
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private LikeBoardRepository likeBoardRepo;

    public void createBoard(BoardDto boardDto) {
        log.info("createBoard 로그  - 진입");

        Member member = memberRepo.findByMemberId(boardDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberException());

        Board board = boardDto.toEntity(member);

        // TODO: set 안쓰는 방법은 없을까
        board.setBlockStatus(BlockStatus.UNBLOCKED);
        boardRepo.save(board);
    }

    public void updateBoard(Long boardId, BoardDto boardDto) {
        log.info("updateBoard 로그  - 진입");

        memberRepo.findByMemberId(boardDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberException());
        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());

        boardRepo.save(board.updateBoardEntity(boardDto));
    }

    public Page<Board> readBoardList(Pageable pageable) {
        log.info("readBoardList 로그  - 진입");
        return boardRepo.findAllByBlockStatus(pageable, BlockStatus.UNBLOCKED);
    }

    public Board readBoardDetails(Long boardId) {
        log.info("readBoardDetails 로그  - 진입");

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        board.increaseHitCount();
        return board;
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        board.updateBoardBlockStatus();

        for(Comment comment : board.getComments()) {
            comment.updateCommentBlockStatus();
        }
        boardRepo.save(board);
    }

    public Page<Board> readMyBoard(Pageable pageable) {
        log.info("readDashboard 로그  - 진입");
        Long memberId = memberService.readSession().getMemberId();
        Page<Board> myBoardList = boardRepo.findByMemberIdAndBlockStatus(pageable, memberId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundMemberException());
        return myBoardList;
    }

    @Transactional
    public void createLikeBoard(Long boardId) {
        log.info("createLikeBoard 로그  - 진입");

        updateLikeCountIncrease(boardId);

        Long memberId = memberService.readSession().getMemberId();
        LikeBoard likeBoard = LikeBoard.builder()
                .memberId(memberId)
                .boardId(boardId)
                .build();
        likeBoardRepo.save(likeBoard);
    }

    // TODO: readSession 안쓰고 파라미터로 받아오는게 나을까?
    public List<LikeBoard> readLikeBoardList() {
        Long memberId = memberService.readSession().getMemberId();
        List<LikeBoard> likeBoardList = likeBoardRepo.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
        return likeBoardList;
    }

    @Transactional
    public void deleteLikeBoard(Long boardId) {
        log.info("deleteLikeBoard 로그  - 진입");

        updateLikeCountDecrease(boardId);

        Long memberId = memberService.readSession().getMemberId();
        LikeBoardPK pk = LikeBoardPK.builder()
                .memberId(memberId)
                .boardId(boardId)
                .build();
        likeBoardRepo.deleteById(pk);
    }

    private void updateLikeCountIncrease(Long boardId) {
        log.info("updateLikeCountPlus 로그  - 진입");

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        board.increaseLikeCount();
        boardRepo.save(board);
    }

    private void updateLikeCountDecrease(Long boardId) {
        log.info("updateLikeCountMinus 로그  - 진입");

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        board.decreaseLikeCount();
        boardRepo.save(board);
    }
}
