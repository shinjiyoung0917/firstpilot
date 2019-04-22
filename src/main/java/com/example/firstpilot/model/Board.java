package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
<<<<<<< HEAD
import java.time.LocalDateTime;

@Entity
=======

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "board")
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
<<<<<<< HEAD
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
=======
    private Long boardId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "member_id", nullable = false, insertable = false, updatable = false)
    private Long memberId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    @Column(name = "updated_date")
    private String updatedDate;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board") //cascade = CascadeType.ALL
    @JsonManagedReference("boardAndComment")
    //@JsonBackReference
    @OrderBy("createdDate, parentId ASC")
    private List<Comment> comments = new ArrayList<>();

     /*@OneToMany(mappedBy = "board") //cascade = CascadeType.ALL
    private List<LikeBoard> likeBoards = new ArrayList<>();
    */
}
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
