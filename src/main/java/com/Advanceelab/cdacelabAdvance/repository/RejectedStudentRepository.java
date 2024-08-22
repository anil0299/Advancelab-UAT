package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.RejectedStudentDtls;

@Repository
public interface RejectedStudentRepository extends JpaRepository<RejectedStudentDtls, Integer>{

	boolean existsByEmailAddress(String emailAddress);
	Page<RejectedStudentDtls> findByEmailAddress(String emailAddress, Pageable pageable);
}
