package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.ExerciseSubmission;

@Repository
public interface ExerciseSubmission_Repo extends JpaRepository<ExerciseSubmission, Long>{		
	
	
//	@Query(value="SELECT EXISTS(SELECT u FROM exercise_submission u WHERE u.userid = :userid AND u.exer_id = :exer_id)",nativeQuery =true) 
//	Boolean checkSubmitted(@Param("userid") Long userId, @Param("exer_id") Long exerId);
	
	@Query(value = "SELECT COUNT(*) FROM exercise_submission WHERE username=?1",nativeQuery = true)
	Integer findByEmail(String emailAddress);
	
	@Query(value = "SELECT * FROM exercise_submission WHERE username = :username AND exer_id = :exerId", nativeQuery = true)
    ExerciseSubmission findByUsernameAndExerciseId(@Param("username") String username, @Param("exerId") Long exerId);
	
	@Query(value="SELECT * FROM exercise_submission WHERE userid = :userId AND exer_id =:exerId",nativeQuery =true) 
	ExerciseSubmission checkSubmitted(Long userId,Long exerId);

	@Query(value = "SELECT exer_id FROM exercise_submission WHERE username = :username", nativeQuery = true)
	List<Long> findExerciseIdByUsername(String username);

}
