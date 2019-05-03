package com.example.firstpilot.repository;

import com.example.firstpilot.model.LikeBoard;
import com.example.firstpilot.util.LikeBoardPK;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {
    Optional<List<LikeBoard> > findByMemberId(Long memberId);
    Optional<LikeBoard> findByMemberIdAndBoardId(Long memberId, Long boardId);
    void deleteByLikeId(Long likeId);
}
