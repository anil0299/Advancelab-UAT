package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.Vm_Master;

@Repository
public interface Vm_MasterRepo extends JpaRepository<Vm_Master, Long>{
	
	@Query(value = "select p.vm_uuid from vm_master p where p.exercise_id= :exid", nativeQuery = true)
	 List<String> findVmuuidByExerciseExid(@Param("exid") Long exid);
	
	@Query(value ="select p.vm_name from vm_master p where p.vm_uuid= :vmuuid" , nativeQuery = true)
	String findVmNameByUuuid(@Param("vmuuid") String vmuuid);
	
	@Query(value = "select p.vm_name from vm_master p where p.exercise_id= :exid", nativeQuery = true)
	 List<String> findVmnameByExerciseExid(@Param("exid") Long exid);
	
	@Query(value = "select * from vm_master where exercise_id = :exerciseId", nativeQuery = true)
	List<Vm_Master> findAllByExerciseId(@Param("exerciseId") Long exerciseId);
	
}
