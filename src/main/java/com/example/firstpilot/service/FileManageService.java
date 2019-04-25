package com.example.firstpilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.util.UUID;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

@Service
public class FileManageService {
    private static final Logger log = LoggerFactory.getLogger(FileManageService.class);

    @Value("${file.upload.directory}")
    private String ABSOLUTE_FILEPATH;

    private Integer IMAGE_CONTENTTYPE_START_INDEX = 0;
    private Integer IMAGE_CONTENTTYPE_END_INDEX = 5;

    /* 서버 디렉토리에 파일 업로드 */
    public ResponseEntity<String> saveBoardFileInDir(MultipartFile uploadFile) {
        log.info("saveBoardFileInDir 로그  - 진입");

        try {
            String originalFileName = uploadFile.getOriginalFilename();
            byte[] bytes = uploadFile.getBytes();

            String uuidFileName = getUuidFileName(originalFileName);
            StringBuffer stringBuffer = new StringBuffer();
            String fileName = stringBuffer.append(new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(System.currentTimeMillis())).append(uuidFileName).toString();

            log.info("saveBoardFileInDir 로그  - fileName : " + fileName);

            File target = new File(ABSOLUTE_FILEPATH, fileName);
            FileCopyUtils.copy(bytes, target);

            if (uploadFile.getContentType().substring(IMAGE_CONTENTTYPE_START_INDEX, IMAGE_CONTENTTYPE_END_INDEX).equals("image")) {
                makeThumbnail(ABSOLUTE_FILEPATH, fileName, originalFileName);
            }
            return new ResponseEntity(fileName, HttpStatus.OK); //fileName;
        } catch(IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private String getUuidFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    private static String makeThumbnail(String uploadRootPath, String fileName, String oriFileName) throws IOException{
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

    /* 파일 객체 가져오기 */
    public ResponseEntity<byte[]> readFileByte(String fileName, HttpServletResponse res) throws IOException {
        BufferedOutputStream out = null;
        InputStream in = null;

        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos + 1);
        byte[] bytes = null;

        try {
            res.setContentType("image/" + ext);
            res.setHeader("Content-Disposition", "inline;filename=" + fileName);
            File file = new File(ABSOLUTE_FILEPATH + "/" + fileName);
            if(file.exists()){
                in = new FileInputStream(file);
                out = new BufferedOutputStream(res.getOutputStream());
                int len;
                bytes = new byte[1024];
                while ((len = in.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null){ out.flush(); }
            if(out != null){ out.close(); }
            if(in != null){ in.close(); }
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(bytes);
    }
}