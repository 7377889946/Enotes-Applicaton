package com.crazycoder.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crazycoder.model.Category;
import com.crazycoder.model.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer>{

	Page<Notes> findByCreatedByAndIsDeletedFalse(Integer user_id, Pageable pageable);

	Optional<Notes> findByIdAndIsDeletedFalse(Integer id);

	List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);

	List<Notes> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime minusdays);



}
