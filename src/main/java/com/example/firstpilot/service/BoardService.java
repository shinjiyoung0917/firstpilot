package com.example.firstpilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.LikeBoard;
import com.example.firstpilot.model.Comment;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.repository.LikeBoardRepository;
import com.example.firstpilot.repository.CommentRepository;
import com.example.firstpilot.util.LikeBoardPK;
import com.example.firstpilot.util.LoginUserDetails;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private LikeBoardRepository likeBoardRepo;
    @Autowired
    private CommentRepository commentRepo;

    /* 로그인한 회원의 세션값 */
    public LoginUserDetails readSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails login = (LoginUserDetails) authentication.getPrincipal();
        return login;
    }

    /* 게시물 정보 삽입 */
    public void createBoard(Board boardData) {
        log.info("createBoard 로그  - 진입");

        // 세션값 가져와서 회원 시퀀스 번호 저장 (프론트에서 바꾸는 것을 대비하기 위해)
        Long memberId = readSession().getMemberId();
        //Member member = this.memberRepo.findByMemberId(memberId);

        Board board = new Board();
        board.setTitle(boardData.getTitle());
        board.setContent(boardData.getContent());
        board.setMemberId(memberId);
        board.setNickname(boardData.getNickname());
        board.setCreatedDate(LocalDateTime.now());
        board.setHitCount((long)0);
        board.setLikeCount((long)0);
        board.setCommentCount((long)0);
        if(boardData.getFilePath().equals("") || boardData.getFilePath() == null) {
            board.setFilePath(null);
        } else {
            board.setFilePath(boardData.getFilePath().substring(6));
        }
        board.setIsValid(1);
        this.boardRepo.save(board);
    }

    /* 서버에 파일 업로드 */
    public String saveBoardFileInDir(MultipartFile uploadFile) throws Exception {
        log.info("saveBoardFileInDir 로그  - 진입");

        String originalFileName = uploadFile.getOriginalFilename();
        byte[] fileData = uploadFile.getBytes();

        // 파일명 중복 방지
        String uuidFileName = getUuidFileName(originalFileName);

        // 파일 업로드 경로 설정
        String rootPath = "./src/main/resources/uploads";

        // 서버에 파일 저장
        File target = new File(rootPath, uuidFileName); // 파일 객체 생성
        FileCopyUtils.copy(fileData, target);

        // 이미지 파일은 썸네일 이미지 생성
        if(uploadFile.getContentType().substring(0, 5).equals("image")) {
            log.info("saveBoardFileInDir 로그  - image 파일입니다");
            uuidFileName = makeThumbnail(rootPath, uuidFileName, originalFileName);
        }

        // 파일 저장 경로 치환
        return replaceSavedFilePath(uuidFileName);

    }

    private String getUuidFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    private static String makeThumbnail(String uploadRootPath, String fileName, String oriFileName) throws Exception {
        int thumbnail_width = 20;
        int thumbnail_height = 30;

        // 원본이미지 메모리에 로드
        BufferedImage originalImg = ImageIO.read(new File(uploadRootPath, fileName));

        String thumbnailName = "thumb_" + fileName;
        String fullPath = uploadRootPath + File.separator + thumbnailName;
        File newFile = new File(fullPath);

        if(!(originalImg.getWidth() <= thumbnail_width && originalImg.getHeight() <= thumbnail_height)){
            //이미지 썸네일로 만들기(그릇 준비 -> 그래픽 만들기 -> 그리기 -> 데이터로 쓰기)
            BufferedImage thumbImage = new BufferedImage(thumbnail_width, thumbnail_height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphic = thumbImage.createGraphics();
            graphic.drawImage(originalImg, 0, 0, thumbnail_width, thumbnail_height, null);

            String extension = FilenameUtils.getExtension(oriFileName);
            ImageIO.write(thumbImage, extension, newFile);
        }
        return thumbnailName;
    }

    private static String replaceSavedFilePath(String fileName) {
        String savedFilePath = fileName;
        return savedFilePath.replace(File.separatorChar, '/');
    }

    /* 게시물 정보 가져오기 */
    public Page<Board> readBoardList(Pageable pageable) {
        log.info("readBoardList 로그  - 진입");
        return this.boardRepo.findAll(pageable);
    }

    /* 좋아요 게시물 목록 가져오기 */
    public List<LikeBoard> readLikeBoardList() {
        Long memberId = readSession().getMemberId();
        return this.likeBoardRepo.findByMemberId(memberId);
    }

    /* 게시물 좋아요 생성 */
    public void createLikeBoard(Long boardId) {
        log.info("createLikeBoard 로그  - 진입");
        Long memberId = readSession().getMemberId();
        LikeBoard likeBoard = new LikeBoard();
        likeBoard.setMemberId(memberId);
        likeBoard.setBoardId(boardId);
        this.likeBoardRepo.save(likeBoard);
    }

    /* 게시물 좋아요 삭제 */
    public void deleteLikeBoard(Long boardId) {
        log.info("deleteLikeBoard 로그  - 진입");
        Long memberId = readSession().getMemberId();

        LikeBoardPK pk = new LikeBoardPK();
        pk.setMemberId(memberId);
        pk.setBoardId(boardId);

        this.likeBoardRepo.deleteById(pk);
    }

    /* 좋아요 및 해제로 게시물의 좋아요 수 업데이트 */
    public void updateLikeCount(Long boardId, Integer status) {
        log.info("updateLikeCount 로그  - 진입");
        if(status == 0) {
            Board board = this.boardRepo.findByBoardId(boardId);
            board.setLikeCount(board.getLikeCount() + 1);
            this.boardRepo.save(board);
        } else {
            Board board = this.boardRepo.findByBoardId(boardId);
            board.setLikeCount(board.getLikeCount() - 1);
            this.boardRepo.save(board);
        }
    }

    /* 상세 게시물 정보 가져오기 */
    public Board readBoardDetails(Long boardId) {
        log.info("readBoardDetails 로그  - 진입");
        return this.boardRepo.findByBoardId(boardId);
    }

    /* 게시물 조회수 업데이트 */
    public void updateHitCount(Long boardId) {
        log.info("updateHitCount 로그  - 진입");
    }

    /* 댓글 정보 삽입 */
    public Comment createComments(Long boardId, Comment commentData) {
        log.info("createComments 로그  - 진입");
        log.info("createComments 로그  - filePath : " + commentData.getFilePath());
        Long memberId = readSession().getMemberId();
        Comment comment = new Comment();
        comment.setBoardId(boardId);
        comment.setContent(commentData.getContent());
        comment.setMemberId(memberId);
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
        comment.setIsValid(1);
        return this.commentRepo.save(comment);
    }

    /* 댓글 정보 가져오기 */
    public List<Comment> readComments(Long boardId) {
        log.info("readComments 로그  - 진입");
        return this.commentRepo.findByBoardId(boardId, new Sort(Sort.Direction.DESC, "createdDate"));
    }
}
