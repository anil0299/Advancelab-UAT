package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.ClassExercise;
import com.Advanceelab.cdacelabAdvance.entity.CreateClass;
import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;

@Repository
public interface CreateClassRepo extends JpaRepository<CreateClass, Integer>{

	boolean existsByClassName(String className);
	
	
	
	@Query(value="select p.class_name from create_class p where p.teacher_id= :teacher_id",nativeQuery = true)
	List<String> findClassNameByEmail(@Param("teacher_id") String teacher_id);

	

	void save(ClassExercise classexercise);
	


}
