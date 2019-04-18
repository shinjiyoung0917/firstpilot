package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "hit_count", nullable = false)
    @ColumnDefault("0")
    private Long hitCount;

    @Column(name = "like_count", nullable = false)
    @ColumnDefault("0")
    private Long likeCount;

    @Column(name = "comment_count", nullable = false)
    @ColumnDefault("0")
    private Long commentCount;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "unblocked", nullable = false)
    @ColumnDefault("1")
    private Integer unblocked;

    /*@OneToMany(mappedBy = "board") //cascade = CascadeType.ALL
    private List<LikeBoard> likeBoards = new ArrayList<>();
    */

    @OneToMany(mappedBy = "board") //cascade = CascadeType.ALL
    @JsonManagedReference
    //@JsonBackReference
    @OrderBy("createdDate, parentId ASC")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;
}