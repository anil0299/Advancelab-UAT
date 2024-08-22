package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.StudentVideoWatchInf;

@Repository
public interface StudentVideoWatchInfRepository extends JpaRepository<StudentVideoWatchInf, Integer>{

	
//	@Query(value = "SELECT * FROM student_video_watch_inf WHERE course_name LIKE '%' || :string || '%" , nativeQuery = true)
//	List<StudentVideoWatchInf> findByCourseType(@Param("string") String string);
//	
	

	@Query(value = "SELECT * FROM student_video_watch_inf WHERE course_name LIKE '%'|| :string || '%'", nativeQuery = true)
	List<StudentVideoWatchInf> findByCourseType1(@Param("string") String string);

	@Query(value="SELECT * FROM student_video_watch_inf WHERE email IN :email_address",nativeQuery = true)
	List<StudentVideoWatchInf> findStudentByClass(@Param("email_address") List<String> email_address);
	
	
	@Query(value="SELECT * FROM student_video_watch_inf WHERE email IN :studlist",nativeQuery = true)
	List<StudentVideoWatchInf> findStudentbyEmail(@Param("studlist") List<String> studlist);
	
	List<StudentVideoWatchInf> findByBatch(int batch);
	
	StudentVideoWatchInf findByUserId(int studentId);
	
	StudentVideoWatchInf findByUserIdAndCourseNameContaining(int userId, String courseName);
	
}