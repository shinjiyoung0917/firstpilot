package com.example.firstpilot.repository;

import com.example.firstpilot.model.Comment;

import com.example.firstpilot.util.BlockStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentIdAndBlockStatus(Long commentId, BlockStatus blockStatus);
    Optional<List<Comment> > findByMemberIdAndBlockStatus(Long memberId, BlockStatus blockStatus);
}
