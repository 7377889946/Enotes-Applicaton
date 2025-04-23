package com.crazycoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crazycoder.model.Todo;

@Repository
public interface ToDoRepository extends JpaRepository<Todo,Integer> {

	List<Todo> findByCreatedBy(Integer userId);

}
