package com.example.firstpilot.repository;

import com.example.firstpilot.model.Comment;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // List<Comment> findByBoardId(Long boardId, Sort sort);
    Comment findByCommentId(Long commentId);
}
