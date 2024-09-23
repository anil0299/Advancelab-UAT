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
	
	@Query("SELECT r FROM RejectedStudentDtls r " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(CAST(r.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(r.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(r.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(r.emailAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(r.qualification) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(r.college) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(r.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(r.registrationDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
	Page<RejectedStudentDtls> searchRejectedStudents(String searchTerm, Pageable pageable);
}
