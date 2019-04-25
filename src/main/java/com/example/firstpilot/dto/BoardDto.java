package com.example.firstpilot.dto;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.Member;
import com.example.firstpilot.util.BlockStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class BoardDto {
    private Long boardId;
    private Long memberId;
    private String nickname;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private String filePath;
    private Long hitCount;
    private Long likeCount;
    private Long commentCount;
    private String createdDate;
    private String updatedDate;
    private BlockStatus blockStatus;

    public Board toEntity(Member member) {
        return Board.builder()
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
}
