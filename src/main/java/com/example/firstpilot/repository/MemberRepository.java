package com.example.firstpilot.repository;

import com.example.firstpilot.model.Member;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(Long userId);
    Member findByNickname(String nickname);
    Member findByEmail(String email);
    void deleteByMemberId(Long memberId);
}