package com.example.firstpilot.service;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.repository.BoardRepository;

import com.example.firstpilot.util.LoginUserDetails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.io.File;

import java.util.UUID;


@Service
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private BoardRepository boardRepo;

    /* 게시물 정보 삽입 */
    public void createBoard(Board boardData) {
        log.info("createBoard 로그  - 진입");

        // 세션값 가져와서 닉네임이랑 시퀀스 파라미터 저장 (프론트에서 바꾸는 것을 대비하기 위해)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails login = (LoginUserDetails) authentication.getPrincipal();
        Long memberId = login.getMemberId();
        //Member member = this.memberRepo.findByMemberId(memberId);

        Board board = new Board();
        board.setTitle(boardData.getTitle());
        board.setContent(boardData.getContent());
        board.setMemberId(memberId);
        board.setNickname(boardData.getNickname());
        board.setCreatedDate(LocalDateTime.now());
        board.setFilePath(boardData.getFilePath());
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
            BufferedImage thumbImage =
                    new BufferedImage(thumbnail_width, thumbnail_height, BufferedImage.TYPE_3BYTE_BGR);
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

}
