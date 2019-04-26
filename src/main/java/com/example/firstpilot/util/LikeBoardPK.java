package com.example.firstpilot.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeBoardPK implements Serializable {
    private Long memberId;
    private Long boardId;
}
