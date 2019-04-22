package com.example.firstpilot.repository;

import com.example.firstpilot.model.MailAuth;
import com.example.firstpilot.util.MailAuthPK;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MailAuthRepository extends JpaRepository<MailAuth, MailAuthPK> {
    void deleteByEmail(String email);
}
