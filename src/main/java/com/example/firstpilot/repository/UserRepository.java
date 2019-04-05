package com.example.firstpilot.repository;

import java.util.List;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import com.example.firstpilot.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
