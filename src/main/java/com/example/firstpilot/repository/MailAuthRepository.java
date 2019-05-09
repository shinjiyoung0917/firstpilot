package com.example.firstpilot.repository;

import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.util.MailAuthPK;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface MailAuthRepository extends JpaRepository<MailAuth, MailAuthPK> {
    Optional<MailAuth> findByEmail(String email);
    void deleteByEmail(String email);
}
