package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "title", nullable = false)
    @Getter @Setter
    private String title;

    @Column(name = "content", nullable = false)
    @Getter @Setter
    private String content;

    @Column(name = "member_id", nullable = false)
    @Getter @Setter
    private Long memberId;

    @Column(name = "nickname", nullable = false)
    @Getter @Setter
    private String nickname;

    @Column(name = "created_date", nullable = false)
    @Getter @Setter
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @Getter @Setter
    private LocalDateTime updatedDate;

    @Column(name = "hit_count", nullable = false)
    @ColumnDefault("0")
    @Getter @Setter
    private Long hitCount;

    @Column(name = "like_count", nullable = false)
    @ColumnDefault("0")
    @Getter @Setter
    private Long likeCount;

    @Column(name = "comment_count", nullable = false)
    @ColumnDefault("0")
    @Getter @Setter
    private Long commentCount;

    @Column(name = "file_path")
    @Getter @Setter
    private String filePath;

    @Column(name = "is_valid", nullable = false)
    @ColumnDefault("1")
    @Getter @Setter
    private Integer isValid;
}
