package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.QuizUserAttempt;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;

@Repository
public interface QuizUserAttemptRepository extends JpaRepository<QuizUserAttempt, Integer>{
	
	@Query(value = "SELECT * FROM p_quiz_user_attempt WHERE published = true", nativeQuery = true)
    List<QuizUserAttempt> findAllPublished();
	
	@Query(value = "SELECT quiz_id FROM p_quiz_user_attempt WHERE user_id=:ans AND published=:status", nativeQuery = true)
	List<Integer> findByUserId(@Param("ans")int i,@Param("status")boolean ans);
	
	Integer countByStudentAndPublishedIsTrue(StudentDtls studentDtls);
	
}
