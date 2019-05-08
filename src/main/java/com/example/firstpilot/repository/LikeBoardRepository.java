package com.example.firstpilot.repository;

import com.example.firstpilot.model.LikeBoard;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> { // LikeBoardPK 대신 Long 사용 (id 컬럼 새로 생성했음)
    Optional<List<LikeBoard> > findByMemberId(Long memberId);
    Optional<LikeBoard> findByMemberIdAndBoardId(Long memberId, Long boardId);
    void deleteByLikeId(Long likeId);
}
