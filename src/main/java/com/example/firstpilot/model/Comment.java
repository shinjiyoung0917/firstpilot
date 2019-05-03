package com.example.firstpilot.model;

import com.example.firstpilot.dto.CommentDto;
import com.example.firstpilot.util.BlockStatus;
import com.example.firstpilot.util.CurrentTime;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "board_id", nullable = false, insertable = false, updatable = false)
    private Long boardId;

    @Column(name = "member_id", nullable = false, insertable = false, updatable = false)
    private Long memberId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "child_count", nullable = false)
    private Long childCount;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    @Column(name = "updated_date")
    private String updatedDate;

    @Column(name = "block_status", nullable = false)
    private BlockStatus blockStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference("boardAndComment")
    private Board board;

    /*public void setBoard(Board board) {
        if(this.board != null) {
            this.board.getComments().remove(this);
        }
        this.board = board;
        board.getComments().add(this);
    }*/

    @PrePersist
    public void prePersist() {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        createdDate = currentTimeString;
        blockStatus = BlockStatus.UNBLOCKED;
    }

    public CommentDto toDto(Board board, Member member) {
        return CommentDto.builder()
                .board(board)
                .boardId(boardId)
                .member(member)
                .memberId(memberId)
                .nickname(nickname)
                .content(content)
                .filePath(filePath)
                .parentId(parentId)
                .childCount(childCount)
                .build();
    }

    public Comment updateCommentEntity(CommentDto commentDto) {
        CurrentTime currentTime = new CurrentTime();
        String currentTimeString = currentTime.getCurrentTime();
        updatedDate = currentTimeString;
        content = commentDto.getContent();
        filePath = commentDto.getFilePath();
        return this;
    }

    public void updateCommentContentAndBlockStatus() {
        content = "삭제된 댓글입니다.";
        blockStatus = BlockStatus.BLOCKED;
    }

    public void increaseChildCount() {
        ++this.childCount;
    }

    public void decreaseChildCount() {
        --this.childCount;
    }

}
