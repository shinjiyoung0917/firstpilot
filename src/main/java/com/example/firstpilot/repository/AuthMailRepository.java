package com.example.firstpilot.repository;

import com.example.firstpilot.model.AuthMail;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface AuthMailRepository extends CrudRepository<AuthMail, Long> { // JpaRepository

}
