package com.example.firstpilot.model;

import com.example.firstpilot.util.LikeBoardPK;
import lombok.Data;

import javax.persistence.*;

@Data
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
}
