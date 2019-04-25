package com.example.firstpilot.service;

import com.example.firstpilot.exceptionAndHandler.NotFoundBoardException;
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
import com.example.firstpilot.util.CurrentTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.example.firstpilot.exceptionAndHandler.NotFoundMemberException;

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

    private CurrentTime currentTime = new CurrentTime();

    /* 게시물 정보 삽입 */
    public void createBoard(BoardDto boardDto) {
        log.info("createBoard 로그  - 진입");

        Member member = memberRepo.findByMemberId(boardDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberException());

        Board board = boardDto.toEntity(member); // member 파라미터로 주기

        // TODO: set 안쓰는 방법은 없을까
        board.setBlockStatus(BlockStatus.UNBLOCKED);
        boardRepo.save(board);
    }

    /* 게시물 업데이트 */
    public void updateBoard(Long boardId, BoardDto boardDto) {
        log.info("updateBoard 로그  - 진입");

        Member member = memberRepo.findByMemberId(boardDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberException());

        Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());

        boardRepo.save(board.updateBoardEntity(boardDto)).toDto();
    }

    /* 전체 게시물 정보 가져오기 */
    public Page<Board> readBoardList(Pageable pageable) {
        log.info("readBoardList 로그  - 진입");
        return this.boardRepo.findAllByBlockStatus(pageable, BlockStatus.UNBLOCKED);
    }

    /* 좋아요 게시물 목록 가져오기 */
    public List<LikeBoard> readLikeBoardList() {
        Long memberId = memberService.readSession().getMemberId();
        return this.likeBoardRepo.findByMemberId(memberId);
    }

    /* 게시물 좋아요 생성 */
    public void createLikeBoard(Long boardId) {
        log.info("createLikeBoard 로그  - 진입");
        Long memberId = memberService.readSession().getMemberId();
        LikeBoard likeBoard = new LikeBoard();
        likeBoard.setMemberId(memberId);
        likeBoard.setBoardId(boardId);
        this.likeBoardRepo.save(likeBoard);
    }

    /* 게시물 좋아요 삭제 */
    public void deleteLikeBoard(Long boardId) {
        log.info("deleteLikeBoard 로그  - 진입");
        Long memberId = memberService.readSession().getMemberId();

        LikeBoardPK pk = new LikeBoardPK();
        pk.setMemberId(memberId);
        pk.setBoardId(boardId);

        this.likeBoardRepo.deleteById(pk);
    }

    /* 좋아요 및 해제로 게시물의 좋아요 수 업데이트 */
    public void updateLikeCount(Long boardId, Integer status) {
        log.info("updateLikeCount 로그  - 진입");
        if(status == 0) {
            Board board = boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                    .orElseThrow(() -> new NotFoundBoardException());
            board.setLikeCount(board.getLikeCount() + 1);
            boardRepo.save(board);
        } else {
            Board board = this.boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                    .orElseThrow(() -> new NotFoundBoardException());
            board.setLikeCount(board.getLikeCount() - 1);
            boardRepo.save(board);
        }
    }

    /* 상세 게시물 및 댓글 정보 가져오기 */
    public Board readBoardDetails(Long boardId) {
        log.info("readBoardDetails 로그  - 진입");
        return boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
    }

    /* 게시물 조회수 업데이트 */
    public void updateHitCount(Long boardId) {
        log.info("updateHitCount 로그  - 진입");
        Board board = this.boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        board.setHitCount(board.getHitCount() + 1);
        this.boardRepo.save(board);
    }

    /* 게시물 삭제 */
    public void deleteBoard(Long boardId) {
        Board board = this.boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        board.setBlockStatus(BlockStatus.BLOCKED);

        for(Comment comment : board.getComments()) {
            comment.setBlockStatus(BlockStatus.BLOCKED);
        }
        this.boardRepo.save(board);
    }

    /* 댓글 수 업데이트 */
    public void updateCommentCount(Long boardId) {
        Board board = this.boardRepo.findByBoardIdAndBlockStatus(boardId, BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());
        board.setCommentCount(board.getCommentCount() + 1);
        this.boardRepo.save(board);
    }

    /* 대시보드 게시물 */
    public Page<Board> readMyBoard(Pageable pageable) {
        log.info("readDashboard 로그  - 진입");
        Long memberId = memberService.readSession().getMemberId();
        return this.boardRepo.findByMemberIdAndBlockStatus(pageable, memberId, BlockStatus.UNBLOCKED);
    }
}
