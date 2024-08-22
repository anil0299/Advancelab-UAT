package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.AddCourse;

@Repository
public interface AddCoursesRepo extends JpaRepository<AddCourse, Integer>{
	
	
	
	@Query(value = "SELECT course FROM Addcourse", nativeQuery = true)
	List<String> findCourse();
	
}
