package com.example.firstpilot.board;

import com.example.firstpilot.model.Board;
import com.example.firstpilot.dto.BoardDto;
import com.example.firstpilot.service.BoardService;
import com.example.firstpilot.repository.BoardRepository;
import com.example.firstpilot.util.BlockStatus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.example.firstpilot.exceptionAndHandler.NotFoundBoardException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@TestPropertySource(properties = {"spring.mail.username=test@test.com"})
public class BoardServiceTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepo;

    @Test
    public void 게시물_수정_테스트() {
        Board board = Board.builder()
                .memberId(92L)
                .nickname("test_nickname")
                .title("test_title")
                .content("test_content")
                .filePath(null)
                .hitCount(0L)
                .likeCount(0L)
                .commentCount(0L)
                .build();
        board = boardRepo.save(board);

        BoardDto boardDto = BoardDto.builder()
                .title("test_modify_title")
                .content("test_modify_content")
                .build();
        boardRepo.save(board.updateBoardEntity(boardDto));

        Board foundBoard = boardRepo.findByBoardIdAndBlockStatus(board.getBoardId(), BlockStatus.UNBLOCKED)
                .orElseThrow(() -> new NotFoundBoardException());

        assertThat(board.getTitle())
                .isEqualTo(foundBoard.getTitle());
        assertThat(board.getContent())
                .isEqualTo(foundBoard.getContent());
    }
}
