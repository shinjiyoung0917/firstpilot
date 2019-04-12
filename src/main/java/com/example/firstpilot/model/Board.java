package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    @Getter
    private Long boardId;

    @Column(name = "title")
    @Getter @Setter
    private String title;

    @Column(name = "content")
    @Getter @Setter
    private String content;

    @Column(name = "member_id")
    @Getter @Setter
    private Long memberId;

    @Column(name = "nickname")
    @Getter @Setter
    private String nickname;

    @Column(name = "created_date")
    @Getter @Setter
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @Getter @Setter
    private LocalDateTime updatedDate;

    @Column(name = "like_count")
    @Getter @Setter
    private Long likeCount;

    @Column(name = "comment_count")
    @Getter @Setter
    private Long commentCount;

    @Column(name = "file_path")
    @Getter @Setter
    private String filePath;

    @Column(name = "is_valid")
    @Getter @Setter
    private Integer isValid;
}
