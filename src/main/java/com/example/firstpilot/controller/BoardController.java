package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.LikeBoard;
import com.example.firstpilot.dto.BoardDto;
import com.example.firstpilot.service.BoardService;
import com.example.firstpilot.service.FileManageService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@RestController
public class BoardController {
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;
    @Autowired
    private FileManageService fileManageService;

    /* 로컬 디렉토리(실제 상용화라고 생각하면 서버 디렉토리)에 저장 요청 */
    @PostMapping("/boards/file")
    public ResponseEntity<String> postBoardFile(@RequestParam("uploadFile") MultipartFile uploadFile) {
        log.info("postBoardFile 로그  - 진입");
        return fileManageService.saveBoardFileInDir(uploadFile);
    }

    /* 게시물 등록 요청 */
    @PostMapping("/boards")
    public void postBoard(@RequestBody BoardDto boardDto) {
        log.info("postBoard 로그  - 진입");
        log.info("postBoard 로그  - board title : " + boardDto.getTitle());
        boardService.createBoard(boardDto);
    }

    /* 전체 게시물 정보 요청 */
    @GetMapping("/boards")
    public Page<Board> getBoardList(@PageableDefault(size = 6, sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return this.boardService.readBoardList(pageable);
    }

    /* 파일 이미지 요청 */
    @GetMapping(value = "/files/{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> getFileStream(@PathVariable String fileName, HttpServletResponse res) {
        try {
            return fileManageService.readFileByte(fileName, res);
        } catch (IOException e) {
            return null;
        }
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

    /* 상세 게시물 및 댓글 정보 요청 */
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
    public void putBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardDto boardDto) {
        log.info("putBoard 로그  - 진입");
        this.boardService.updateBoard(boardId, boardDto);
    }

    /* 게시물 삭제 요청 */
    @DeleteMapping("/boards/{boardId}")
    public void deleteBoard(@PathVariable("boardId") Long boardId) {
        log.info("deleteBoard 로그  - 진입");
        this.boardService.deleteBoard(boardId);
    }

    /* 대시보드 (본인이 작성한 글) 정보 요청 */
    @GetMapping("/dashboards/boards")
    public Page<Board> getMyBoard(@PageableDefault(size = 6, sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("getMyBoard 로그 - 진입");
        return this.boardService.readMyBoard(pageable);
    }
}
