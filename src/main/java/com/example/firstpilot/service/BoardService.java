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
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    //private final static String savedPath = "./src/main/resources/uploads";

    @Value("${file.upload.directory}")
    String ABSOLUTE_FILEPATH;           // 파일 업로드 경로

    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private LikeBoardRepository likeBoardRepo;
    @Autowired
    private MemberService memberService;


    /* 서버 디렉토리에 파일 업로드 */
    public String saveBoardFileInDir(MultipartFile uploadFile) throws Exception {
        log.info("saveBoardFileInDir 로그  - 진입");

        String originalFileName = uploadFile.getOriginalFilename();
        byte[] bytes = uploadFile.getBytes();

        // 파일명 중복 방지
        String uuidFileName = getUuidFileName(originalFileName);
        StringBuffer stringBuffer = new StringBuffer();
        String fileName = stringBuffer.append(new SimpleDateFormat("yyyyMMddHHmmss")
                .format(System.currentTimeMillis())).append(uuidFileName).toString();

        log.info("saveBoardFileInDir 로그  - fileName : " + fileName);

        /*// 디렉토리에 파일 저장
        Path path = Paths.get(ABSOLUTE_FILEPATH + fileName);
        Files.write(path, bytes);
        */

        // 디렉토리에 파일 저장
        File target = new File(ABSOLUTE_FILEPATH, fileName); // 파일 객체 생성
        FileCopyUtils.copy(bytes, target);

        // 이미지 파일은 썸네일 이미지 생성
        if(uploadFile.getContentType().substring(0, 5).equals("image")) {
            log.info("saveBoardFileInDir 로그  - image 파일입니다");
            makeThumbnail(ABSOLUTE_FILEPATH, fileName, originalFileName);
        }

        return fileName;

        /*byte[] fileData = uploadFile.getBytes();
        log.info("saveBoardFileInDir 로그  - fileData : " + fileData);*/


        //return fileName;
        // 파일 저장 경로에서 구분자 치환
        //return replaceSavedFilePath(fileName);

    }

    private String getUuidFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    private static String makeThumbnail(String uploadRootPath, String fileName, String oriFileName) throws Exception {
        log.info("makeThumbnail 로그  - uploadRootPath : " + uploadRootPath);
        log.info("makeThumbnail 로그  - fileName : " + fileName);
        log.info("makeThumbnail 로그  - oriFileName : " + oriFileName);
        int thumbnail_width = 700;
        int thumbnail_height = 400;

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

    public  static String replaceSavedFilePath(String filePath) {
        String savedFilePath = filePath;
        return savedFilePath.replace(File.separatorChar, '/');  // \를 /로 치환
    }

    /*public static String getBase64String(String filePath) {
        log.info("getBase64String 로그  - 진입");
        String fileExt = filePath.substring(filePath.lastIndexOf('.') + 1);
        String result = "data:image/" + fileExt + ";base64," + filePath;

        log.info("getBase64String 로그  - result : " + result);

        String imageString;
        String fileExt = filePath.substring(filePath.lastIndexOf('.') + 1);

        log.info("getBase64String 로그  - fileExt : " + fileExt);

        FileInputStream inputStream = null;
        ByteArrayOutputStream byteOutStream = null;

        try {
            File file = new File(filePath);

            log.info("getBase64String 로그  - 1111");

            if( file.exists() ) {
                inputStream = new FileInputStream(file);
                byteOutStream = new ByteArrayOutputStream();

                int len = 0;
                byte[] buf = new byte[1024];
                while((len = inputStream.read(buf)) != -1) {
                    byteOutStream.write( buf, 0, len);
                }

                log.info("getBase64String 로그  - 222");

                byte[] fileArray = byteOutStream.toByteArray();
                imageString = new String(Base64.encodeBase64(fileArray));

                log.info("getBase64String 로그  - 333");

                String changeString = "data:image/" + fileExt + ";base64," + imageString;
                result = result.replace(filePath, changeString);

                log.info("getBase64String 로그  - changeString : " + changeString);
                log.info("getBase64String 로그  - result : " + result);
            }

            inputStream.close();
            byteOutStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return result;
    }*/

    /* 게시물 정보 삽입 */
    public void createBoard(Board boardData) {
        log.info("createBoard 로그  - 진입");

        // 세션값 가져와서 회원 시퀀스 번호 저장 (프론트에서 바꾸는 것을 대비하기 위해)
        Long memberId = memberService.readSession().getMemberId();

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
            board.setFilePath(boardData.getFilePath());
        }
        board.setIsNotBlocked(1);
        this.boardRepo.save(board);
    }

    /* 전체 게시물 정보 가져오기 */
    public Page<Board> readBoardList(Pageable pageable) {
        log.info("readBoardList 로그  - 진입");
        return this.boardRepo.findAllByIsNotBlocked(pageable, 1);
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
            Board board = this.boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
            board.setLikeCount(board.getLikeCount() + 1);
            this.boardRepo.save(board);
        } else {
            Board board = this.boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
            board.setLikeCount(board.getLikeCount() - 1);
            this.boardRepo.save(board);
        }
    }

    /* 상세 게시물 및 댓글 정보 가져오기 */
    public Board readBoardDetails(Long boardId) {
        log.info("readBoardDetails 로그  - 진입");
        return this.boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
    }

    /* 게시물 조회수 업데이트 */
    public void updateHitCount(Long boardId) {
        log.info("updateHitCount 로그  - 진입");
        Board board = this.boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
        board.setHitCount(board.getHitCount() + 1);
        this.boardRepo.save(board);
    }

    /* 게시물 업데이트 */
    public void updateBoard(Long boardId, Board boardData) {
        log.info("updateBoard 로그  - 진입");
        Board board = boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
        board.setTitle(boardData.getTitle());
        board.setContent(boardData.getContent());
        board.setCreatedDate(boardData.getCreatedDate());
        board.setUpdatedDate(LocalDateTime.now() );
        board.setLikeCount(boardData.getLikeCount());
        board.setCommentCount(boardData.getCommentCount());
        board.setHitCount(boardData.getHitCount());
        if(boardData.getFilePath().equals("") || boardData.getFilePath() == null) {
            board.setFilePath(null);
        } else {
            board.setFilePath(boardData.getFilePath());
        }
        board.setIsNotBlocked(boardData.getIsNotBlocked());
        this.boardRepo.save(board);
    }

    /* 게시물 삭제 */
    public void deleteBoard(Long boardId) {
        Board board = this.boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
        board.setIsNotBlocked(0);        // 블라인드 되도록 유효상태 변경

        for(Comment comment : board.getComments()) {
            comment.setIsNotBlocked(0);
        }
        this.boardRepo.save(board);
    }

    /* 댓글 수 업데이트 */
    public void updateCommentCount(Long boardId) {
        Board board = this.boardRepo.findByBoardIdAndIsNotBlocked(boardId, 1);
        board.setCommentCount(board.getCommentCount() + 1);
        this.boardRepo.save(board);
    }
}
