package com.example.firstpilot.dto;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.Member;
import com.example.firstpilot.util.BlockStatus;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long boardId;
    private Member member;
    private Long memberId;
    private String nickname;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 30, min = 1, message = "제목의 글자 수는 1자 이상 30자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 2000, min = 1, message = "내용의 글자 수는 1자 이상 2000자 이하여야 합니다.")
    private String content;

    private String filePath;

    @Builder.Default
    private Long hitCount = 0L;
    @Builder.Default
    private Long likeCount = 0L;
    @Builder.Default
    private Long commentCount = 0L;
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
