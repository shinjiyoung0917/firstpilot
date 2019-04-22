package com.example.firstpilot.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeBoardPK implements Serializable {
    private Long memberId;
    private Long boardId;
}
