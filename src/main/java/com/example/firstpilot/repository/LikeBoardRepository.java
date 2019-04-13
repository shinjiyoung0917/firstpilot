package com.example.firstpilot.repository;

import com.example.firstpilot.model.LikeBoard;
import com.example.firstpilot.util.LikeBoardPK;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface LikeBoardRepository extends JpaRepository<LikeBoard, LikeBoardPK> {
    List<LikeBoard> findByMemberId(Long memberId);
}
