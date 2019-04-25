package com.example.firstpilot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.example.firstpilot.dto.BoardDto;
import com.example.firstpilot.util.BlockStatus;
import com.example.firstpilot.util.CurrentTime;

import javax.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @Column(name = "block_status", nullable = false)
    private BlockStatus blockStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    @JsonManagedReference("boardAndComment")
    @OrderBy("createdDate, parentId ASC")
    private List<Comment> comments = new ArrayList<>();

     /*@OneToMany(mappedBy = "board") //cascade = CascadeType.ALL
    private List<LikeBoard> likeBoards = new ArrayList<>();
    */

    @PrePersist
    public void prePersist() {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        updatedDate = currentTimeString;
    }

    @Builder
    public Board(Member member, Long memberId, String nickname, String title, String content, String filePath, Long hitCount, Long likeCount, Long commentCount) {
        this.member = member;
        this.memberId = memberId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.filePath = filePath;
        this.hitCount = hitCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public BoardDto toDto() {
        return new BoardDto();
    }

    public Board updateBoardEntity(BoardDto boardDto) {
        title = boardDto.getTitle();
        content = boardDto.getContent();
        filePath = boardDto.getFilePath();
        return this;
    }



}
