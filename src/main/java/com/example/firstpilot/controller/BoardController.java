package com.example.firstpilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.LikeBoard;
import com.example.firstpilot.dto.BoardDto;
import com.example.firstpilot.service.BoardService;
import com.example.firstpilot.service.FileManageService;

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

import javax.validation.Valid;

import java.util.List;

import java.io.IOException;

@RestController
public class BoardController {
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;
    @Autowired
    private FileManageService fileManageService;

    @PostMapping("/boards/file")
    public ResponseEntity<String> postBoardFile(@RequestParam("uploadFile") MultipartFile uploadFile) {
        log.info("postBoardFile 로그  - 진입");

        return fileManageService.saveBoardFileInDir(uploadFile);
    }

    @PostMapping("/boards")
    public void postBoard(@RequestBody @Valid BoardDto boardDto) {
        log.info("postBoard 로그  - 진입");

        boardService.createBoard(boardDto);
    }

    @GetMapping("/boards")
    public Page<Board> getBoardList(@PageableDefault(size = 6, sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return boardService.readBoardList(pageable);
    }

   // TODO: JPG 파일은 왜 안되는 건가..?
    @GetMapping(value = "/files/{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> getFileStream(@PathVariable String fileName, HttpServletResponse res) {
        try {
            return fileManageService.readFileByte(fileName, res);
        } catch (IOException e) {
            // TODO: 수정
            return null;
        }
    }

    @GetMapping("/boards/likes")
    public List<LikeBoard> getLikeBoardList() {
        return boardService.readLikeBoardList();
    }

    @PostMapping("/boards/{boardId}/like")
    public void postLikeBoard(@PathVariable("boardId") Long boardId) {
        log.info("postLikeBoard 로그  - 진입");

        boardService.createLikeBoard(boardId);
    }

    @DeleteMapping("/boards/{boardId}/like")
    public void deleteLikeBoard(@PathVariable("boardId") Long boardId) {
        log.info("deleteLikeBoard 로그  - 진입");

        boardService.deleteLikeBoard(boardId);
    }

    @GetMapping("/boards/{boardId}")
    public Board getBoardDetails(@PathVariable("boardId") Long boardId) {
        log.info("getBoardDetails 로그  - 진입");

        return boardService.readBoardDetails(boardId);

    }

    @PutMapping("/boards/{boardId}")
    public void putBoard(@PathVariable("boardId") Long boardId, @RequestBody @Valid BoardDto boardDto) {
        log.info("putBoard 로그  - 진입");

        boardService.updateBoard(boardId, boardDto);
    }

    @DeleteMapping("/boards/{boardId}")
    public void deleteBoard(@PathVariable("boardId") Long boardId) {
        log.info("deleteBoard 로그  - 진입");

        boardService.deleteBoard(boardId);
    }

    @GetMapping("/dashboards/boards")
    public Page<Board> getMyBoard(@PageableDefault(size = 6, sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("getMyBoard 로그 - 진입");

        return boardService.readMyBoard(pageable);
    }
}
