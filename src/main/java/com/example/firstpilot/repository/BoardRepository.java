package com.example.firstpilot.repository;

import com.example.firstpilot.model.Board;

import com.example.firstpilot.util.BlockStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByBlockStatus(Pageable pageable, BlockStatus blockStatus);
    Optional<Board> findByBoardIdAndBlockStatus(Long boardId, BlockStatus blockStatus);
    Optional<Page<Board> > findByMemberIdAndBlockStatus(Pageable pageable, Long memberId, BlockStatus blockStatus);
    Optional<List<Board> > findAllByMemberIdAndBlockStatus(Long memberId, BlockStatus blockStatus);
}


