package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Advanceelab.cdacelabAdvance.entity.Quiz;

public interface QuizRepo extends JpaRepository<Quiz, Integer>{

	@Query("SELECT COUNT(q) FROM Quiz q WHERE q.published = true") 
    long countPublishedQuizzes();
	
	List<Quiz> findByPublishedIsTrue();
	
}
