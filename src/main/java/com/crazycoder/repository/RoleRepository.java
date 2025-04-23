package com.crazycoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crazycoder.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
}
