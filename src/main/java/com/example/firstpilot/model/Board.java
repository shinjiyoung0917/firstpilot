package com.example.firstpilot.model;

import com.example.firstpilot.dto.BoardDto;
import com.example.firstpilot.util.BlockStatus;
import com.example.firstpilot.util.CurrentTime;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Long hitCount;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Column(name = "comment_count", nullable = false)
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

    @OneToMany(mappedBy = "board")
    @JsonManagedReference("memberBoardAndLikeBoard")
    private List<LikeBoard> likeBoards = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        createdDate = currentTimeString;
        blockStatus = BlockStatus.UNBLOCKED;
    }

    @PreUpdate
    public void preUpdate() {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        updatedDate = currentTimeString;
    }

    public BoardDto toDto(Member member) {
        return BoardDto.builder()
                .member(member)
                .memberId(memberId)
                .nickname(nickname)
                .title(title)
                .content(content)
                .filePath(filePath)
                .hitCount(hitCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .filePath(filePath)
                .build();
    }

    public Board updateBoardEntity(BoardDto boardDto) {
        title = boardDto.getTitle();
        content = boardDto.getContent();
        filePath = boardDto.getFilePath();
        return this;
    }

    public void increaseHitCount() {
        ++this.hitCount;
    }

    public void increaseLikeCount() {
        ++this.likeCount;
    }

    public void decreaseLikeCount() {
        --this.likeCount;
    }

    public void increaseCommentCount() {
        ++this.commentCount;
    }

    public void decreaseCommentCount() {
        --this.commentCount;
    }

    public void updateBoardBlockStatus() {
        blockStatus = BlockStatus.BLOCKED;
    }

}
