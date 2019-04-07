package com.example.firstpilot.repository;

import com.example.firstpilot.model.Board;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {

}


