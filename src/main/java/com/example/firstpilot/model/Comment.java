package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ManyToAny;

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

    @ManyToOne
    @JoinColumn(name = "board_id")
    //@Column(name = "board_id", nullable = false)
    @Getter @Setter
    private Long boardId;

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

    @Column(name = "parent_id")
    @Getter @Setter
    private Long parentId;

    @Column(name = "child_count", nullable = false)
    @ColumnDefault("0")
    @Getter @Setter
    private Long childCount;

    @Column(name = "file_path")
    @Getter @Setter
    private String filePath;

    @Column(name = "is_valid", nullable = false)
    @ColumnDefault("1")
    @Getter @Setter
    private Integer isValid;

    /*@ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;*/
}
