package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "board_id", nullable = false, insertable = false, updatable = false)
    private Long boardId;

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

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "child_count", nullable = false)
    @ColumnDefault("0")
    private Long childCount;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "unblocked", nullable = false)
    @ColumnDefault("1")
    private Integer unblocked;

    @ManyToOne
    @JoinColumn(name = "member_id") // insertable = false, updatable = false
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference("boardAndComment")
    //@JsonManagedReference
    //@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@Id")
    private Board board;

    /*public void setMember(Member member) {
        // 무한루프 발생 방지
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