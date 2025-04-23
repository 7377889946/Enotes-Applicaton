package com.crazycoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crazycoder.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Boolean existsByEmail(String email);

}
