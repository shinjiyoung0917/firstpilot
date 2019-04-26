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
@IdClass(LikeBoardPK.class)
@Table(name = "like_board")
public class LikeBoard {
    @Id
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Id
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference("boardAndLikeBoard")
    private Board board;
}
