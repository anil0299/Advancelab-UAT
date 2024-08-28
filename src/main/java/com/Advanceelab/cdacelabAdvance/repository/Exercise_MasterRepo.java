package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;


@Repository
public interface Exercise_MasterRepo extends JpaRepository<Exercise_Master,Long>{

	@Query(value="select u.exercise_name FROM Exercise u WHERE u.Ex_id = :Ex_id", nativeQuery = true)
	String findExerciseByEx_id(@Param("Ex_id") Long Ex_id);
	
	@Query(value="select u.Ex_id FROM Exercise u WHERE u.exercise_name = :exercise_name",nativeQuery = true)
	long findExidByProbstate(@Param("exercise_name") String exercise_name);
	
	
	@Query(value="select u.Ex_id FROM Exercise u WHERE u.exercise_name = :exercise_name",nativeQuery = true)
	long findExidByExer_name(@Param("exercise_name") String exercise_name);
	
	@Query(value = "SELECT e FROM Exercise_Master e WHERE e.Ex_id IN :exIds")
	List<Exercise_Master> findByEx_idIn(List<Long> exIds);

    @Query(value = "select ex_id from exercise where topic_id =:topicId",nativeQuery = true)
    public Long findByTopicId(int topicId);

}
