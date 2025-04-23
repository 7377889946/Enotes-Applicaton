package com.crazycoder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crazycoder.model.Category;
import com.crazycoder.model.FavoriteNotes;

public interface FavoriteNotesRepository extends JpaRepository<FavoriteNotes, Integer> {

	Optional<FavoriteNotes> findByUserId(Integer userId);

}
