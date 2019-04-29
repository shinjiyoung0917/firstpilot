package com.example.firstpilot.model;

import com.example.firstpilot.util.LikeBoardPK;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@IdClass(LikeBoardPK.class)
@Table(name = "like_board")
public class LikeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "like_id")
    private Long likeId;

    @Column(name = "member_id", nullable = false, insertable = false, updatable = false)
    private Long memberId;

    @Column(name = "board_id", nullable = false, insertable = false, updatable = false)
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference("memberBoardAndLikeBoard")
    private Board board;

}
