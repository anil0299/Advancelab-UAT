package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.ExerciseContent;

@Repository
public interface ExerciseContentRepository extends JpaRepository<ExerciseContent, Integer>{

	@Query(value = "select * from exercise_content where exercise_id = ?1",nativeQuery = true)
	public ExerciseContent findByExerciseId(int Exid);

	@Query("SELECT e.exerciseId FROM ExerciseContent e")
	public List<Long> findAllExerciseIds();
}
