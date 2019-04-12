package com.example.firstpilot.repository;

import com.example.firstpilot.model.Board;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}


