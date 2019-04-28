package com.example.firstpilot.dto;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.model.Comment;
import com.example.firstpilot.model.Member;
import com.example.firstpilot.util.BlockStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private Board board;
    private Long boardId;
    private Member member;
    private Long memberId;
    private String nickname;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private String filePath;
    private Long parentId;

    @Builder.Default
    private Long childCount = 0L;

    private String createdDate;
    private String updatedDate;
    private BlockStatus blockStatus;

    public Comment toEntity(Board board, Member member) {
        return Comment.builder()
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
}
