package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.AdvanceLabUserVmDetails;

@Repository
public interface AdvanceLabUserVmDetailsRepository extends JpaRepository<AdvanceLabUserVmDetails, Long>{
	
	@Query(value = "select vm_name from advance_lab_user_vm_details where username =:username and exercise_id =:exerciseId",nativeQuery = true)
	Set<String> findByUsername(String username, Long exerciseId);
	
	@Query(value = "select * from advance_lab_user_vm_details where username =:username and exercise_id =:exerciseId",nativeQuery = true)
	List<AdvanceLabUserVmDetails> findAllByUsername(String username,Long exerciseId);
	
	List<AdvanceLabUserVmDetails> findAllByExerciseId(Long exerciseId);
	
	List<AdvanceLabUserVmDetails> findByUsername(String username);

}
