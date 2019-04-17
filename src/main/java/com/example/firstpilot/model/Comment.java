package com.example.firstpilot.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private Board board;
    /*@Column(name = "board_id", nullable = false)
    private Long boardId;
    */

    @Column(name = "content", nullable = false)
    private String content;

    /*@ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    */
    @Column(name = "member_id", nullable = false)
    private Long memberId;


    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "child_count", nullable = false)
    @ColumnDefault("0")
    private Long childCount;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "is_valid", nullable = false)
    @ColumnDefault("1")
    private Integer isValid;

    /*public void setMember(Member member) {
    if(this.member != null) {
            this.member.getComments().remove(this);
        }
        this.member = member;
        member.getComments().add(this);
    }*/

    public void setBoard(Board board) {
        // 무한루프 발생 방지
        if(this.board != null) {
            this.board.getComments().remove(this);
        }
        this.board = board;
        board.getComments().add(this);
    }
}
