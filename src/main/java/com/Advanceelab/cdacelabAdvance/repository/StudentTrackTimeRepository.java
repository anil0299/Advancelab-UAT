package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.StudentTrackTime;


@Repository
public interface StudentTrackTimeRepository extends JpaRepository<StudentTrackTime, Long>{
	
	StudentTrackTime findByUsername(String username);
}
