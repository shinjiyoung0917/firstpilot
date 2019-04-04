package com.example.firstpilot.repository;

//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import com.example.firstpilot.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

}


/*public interface UserRepository extends CrudRepository<User, String> {

}*/
