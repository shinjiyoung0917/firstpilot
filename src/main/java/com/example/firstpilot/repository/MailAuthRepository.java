package com.example.firstpilot.repository;

import com.example.firstpilot.model.MailAuth;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface MailAuthRepository extends JpaRepository<MailAuth, Long> {
    List<MailAuth> findByEmailAndAuthType(String email, Integer authType);
}
