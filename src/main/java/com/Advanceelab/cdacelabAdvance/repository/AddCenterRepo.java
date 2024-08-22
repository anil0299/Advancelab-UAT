package com.Advanceelab.cdacelabAdvance.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.CenterDetails;



@Repository
public interface AddCenterRepo extends JpaRepository<CenterDetails, Integer>{
	
	
	
	@Query(value="Select DISTINCT state FROM center_details", nativeQuery=true)
	List<String> findDistinctState();
	
	
	@Query(value="Select u.college FROM center_details u WHERE u.state = :state",nativeQuery = true)
	 List<String> findCollegesByState(@Param("state") String state);

	 

}
