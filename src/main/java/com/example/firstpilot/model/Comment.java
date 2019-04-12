package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    @Getter
    private Long commentId;

    @Column(name = "board_id")
    @Getter @Setter
    private Long boardId;

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

    @Column(name = "parent_id")
    @Getter @Setter
    private Long parentId;

    @Column(name = "child_count")
    @Getter @Setter
    private Long childCount;

    @Column(name = "file_path")
    @Getter @Setter
    private String filePath;

    @Column(name = "is_valid")
    @Getter @Setter
    private Integer isValid;
}
