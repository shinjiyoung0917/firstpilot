package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    @Getter @Setter
    private Integer boardID;

    @Column(name = "title")
    @Getter @Setter
    private String title;

    @Column(name = "content")
    @Getter @Setter
    private String content;

    @Column(name = "user_id")
    @Getter @Setter
    private String userID;

    @Column(name = "nickname")
    @Getter @Setter
    private String nickname;

    @Column(name = "board_date")
    @Getter @Setter
    private LocalDateTime boardDate;

    @Column(name = "like_cnt")
    @Getter @Setter
    private Integer likeCnt;

    @Column(name = "comment_cnt")
    @Getter @Setter
    private Integer commentCnt;

    @Column(name = "filepath")
    @Getter @Setter
    private String filePath;

    @Column(name = "valid")
    @Getter @Setter
    private Integer valid;
}
