package com.example.firstpilot.repository;

import com.example.firstpilot.model.Member;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(Long userId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);
    void deleteByMemberId(Long memberId);
}
