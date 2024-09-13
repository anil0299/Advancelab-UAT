package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Query("SELECT s FROM StudentVideoWatchInf s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(CAST(s.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.userId AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.courseId AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.courseName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.college) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.batch AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(TO_CHAR(s.percetageStatus, 'FM999999999.00')) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
		       "AND s.courseName LIKE %:course%")
	Page<StudentVideoWatchInf> searchBasicCompletion(@Param("searchTerm") String searchTerm, @Param("course") String course, Pageable pageable);
	
}