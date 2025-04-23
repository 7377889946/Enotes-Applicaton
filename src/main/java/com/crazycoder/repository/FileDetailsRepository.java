package com.crazycoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crazycoder.model.FileDetails;

@Repository
public interface FileDetailsRepository extends JpaRepository<FileDetails, Integer> {

	

	

}
