package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.ClassExercise;
import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;

@Repository
public interface ClassExerciseRepository extends JpaRepository<ClassExercise, Integer>{
	
	
	@Query(value="SELECT EXISTS(SELECT u FROM class_exercise u WHERE u.class_name = :class_name AND u.exer_id = :exer_id)",nativeQuery =true)
	Boolean checkClassExercise(@Param("class_name") String class_name, @Param("exer_id") Long exerId);

	@Query(value = "SELECT e FROM ClassExercise e WHERE e.class_name IN :className")
	List<ClassExercise> findByClass_nameIn(@Param("className") List<String> className);

	
	@Query("SELECT u.exer_id FROM ClassExercise u WHERE u.class_name = :class_name")
	List<Long> findExerIdbyClassName(@Param("class_name") String classname);
	
	@Query(value = "SELECT COUNT(*) FROM Class_Exercise WHERE class_name=?1",nativeQuery = true)
	Integer getByClassName(String className);
}
