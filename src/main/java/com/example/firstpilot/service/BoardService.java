package com.example.firstpilot.service;

import com.example.firstpilot.exceptionAndHandler.UnableToDeleteLikeBoardException;
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
import com.example.firstpilot.util.BlockStatus;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

import com.example.firstpilot.exceptionAndHandler.NotFoundResourcesException;

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
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 회원입니다."));

        Board board = boardDto.toEntity(member);
        boardRepo.save(board);
    }

    public void updateBoard(Long boardId, BoardDto boardDto) {
        log.info("updateBoard 로그  - 진입");

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));

        boardRepo.save(board.updateBoardEntity(boardDto));
    }

    public Page<Board> readBoardList(Pageable pageable) {
        log.info("readBoardList 로그  - 진입");

        return boardRepo.findAllByBlockStatus(pageable, BlockStatus.UNBLOCKED);
    }

    public Board readBoardDetails(Long boardId) {
        log.info("readBoardDetails 로그  - 진입");

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));
        board.increaseHitCount();
        boardRepo.save(board);
        return board;
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));
        board.updateBoardBlockStatus();

        for(Comment comment : board.getComments()) {
            comment.updateCommentContentAndBlockStatus();
        }
        boardRepo.save(board);
    }

    public Page<Board> readMyBoard(Pageable pageable) {
        log.info("readDashboard 로그  - 진입");
        Long memberId = memberService.readSession().getMemberId();
        Page<Board> myBoardList = boardRepo.findByMemberIdAndBlockStatus(pageable, memberId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 회원입니다."));
        return myBoardList;
    }

    @Transactional
    public void createLikeBoard(Long boardId) {
        log.info("createLikeBoard 로그  - 진입");

        updateLikeCountIncrease(boardId);

        Member member = memberService.readSession();
        Long memberId = member.getMemberId();
        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));

        LikeBoard likeBoard = LikeBoard.builder()
                .memberId(memberId)
                .boardId(boardId)
                .member(member)
                .board(board)
                .build();
        likeBoardRepo.save(likeBoard);
    }

    @Transactional
    public void deleteLikeBoard(Long boardId) {
        log.info("deleteLikeBoard 로그  - 진입");

        updateLikeCountDecrease(boardId);

        Long memberId = memberService.readSession().getMemberId();

        LikeBoard likeBoard = likeBoardRepo.findByMemberIdAndBoardId(memberId, boardId)
                .orElseThrow(UnableToDeleteLikeBoardException::new);
        likeBoardRepo.deleteByLikeId(likeBoard.getLikeId());
    }

    @Transactional
    public void updateLikeCountIncrease(Long boardId) {
        log.info("updateLikeCountIncrease 로그  - 진입");

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));
        board.increaseLikeCount();
        boardRepo.save(board);
    }

    @Transactional
    public void updateLikeCountDecrease(Long boardId) {
        log.info("updateLikeCountDecrease 로그  - 진입");

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 게시물입니다."));
        board.decreaseLikeCount();
        boardRepo.save(board);
    }
}
