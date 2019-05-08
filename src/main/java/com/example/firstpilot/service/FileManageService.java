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
import java.util.Optional;
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

    public ResponseEntity<String> saveBoardFileInDir(MultipartFile uploadFile) {
        log.info("saveBoardFileInDir 로그  - 진입");

        File folder = new File(ABSOLUTE_FILEPATH);
        if(!folder.exists()) {
            try{
                folder.mkdir();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        try {
            String originalFileName = uploadFile.getOriginalFilename();
            byte[] bytes = uploadFile.getBytes();

            String uuidFileName = generateUuidFileName(originalFileName);
            StringBuffer stringBuffer = new StringBuffer();
            String fileName = stringBuffer.append(new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(System.currentTimeMillis())).append(uuidFileName).toString();

            log.info("saveBoardFileInDir 로그  - fileName : " + fileName);

            File target = new File(ABSOLUTE_FILEPATH, fileName);
            FileCopyUtils.copy(bytes, target);

            String nullableContentType = uploadFile.getContentType();
            Optional.ofNullable(nullableContentType)
                    .ifPresent(value -> {
                        if (nullableContentType.substring(IMAGE_CONTENTTYPE_START_INDEX, IMAGE_CONTENTTYPE_END_INDEX).equals("image")) {
                            try {
                                makeThumbnail(ABSOLUTE_FILEPATH, fileName, originalFileName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            return new ResponseEntity(fileName, HttpStatus.OK);
        } catch(IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private String generateUuidFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    private String makeThumbnail(String uploadRootPath, String fileName, String oriFileName) throws IOException{
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
            // 썸네일 만들기(그릇 준비 -> 그래픽 만들기 -> 그리기 -> 데이터로 쓰기)
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
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos + 1);
        byte[] bytes = null;

        res.setContentType("image/" + ext);
        res.setHeader("Content-Disposition", "inline;filename=" + fileName);
        File file = new File(ABSOLUTE_FILEPATH + "/" + fileName);

        if(file.exists()) {
            try(InputStream in = new FileInputStream(file);
                BufferedOutputStream out = new BufferedOutputStream(res.getOutputStream());
            ) {
                int len;
                bytes = new byte[1024];
                while ((len = in.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: catch 하고 수행할 로직 작성하기
            }
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(bytes);
    }
}
