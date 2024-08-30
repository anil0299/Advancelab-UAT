package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.RejectedStudentDtls;

@Repository
public interface RejectedStudentRepository extends JpaRepository<RejectedStudentDtls, Integer>{

	boolean existsByEmailAddress(String emailAddress);
	
	@Query("SELECT COUNT(e) > 0 FROM RejectedStudentDtls e WHERE e.emailAddress LIKE :username%")
	boolean existsByEmailAddressContaining(@Param("username") String username);
	
	Page<RejectedStudentDtls> findByEmailAddress(String emailAddress, Pageable pageable);
}
