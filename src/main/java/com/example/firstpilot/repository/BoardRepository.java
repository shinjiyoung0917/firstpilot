package com.example.firstpilot.repository;

//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import com.example.firstpilot.model.Board;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {

}


