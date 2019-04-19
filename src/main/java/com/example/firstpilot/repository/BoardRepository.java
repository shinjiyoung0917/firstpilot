package com.example.firstpilot.repository;

import com.example.firstpilot.model.Board;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByUnblocked(Pageable pageable, Integer unblocked);
    Board findByBoardIdAndUnblocked(Long boardId, Integer unblocked);
    Page<Board> findByMemberIdAndUnblocked(Pageable pageable, Long memberId, Integer unblocked);
    List<Board> findAllByMemberIdAndUnblocked(Long memberId, Integer unblocked);
}


