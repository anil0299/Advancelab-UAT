package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.CreateClass;
import com.Advanceelab.cdacelabAdvance.entity.TeacherDtls;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherDtls, Integer>{



		public boolean existsByEmailAddress(String email);

	List<TeacherDtls> findByApproved(boolean approved);

	/*
	 * @Query(
	 * value="select u.category_certificate FROM TeacherDtls u WHERE u.id = :id"
	 * ,nativeQuery = true) public byte[] findCertificate(@Param("id") int id);
	 */

	public TeacherDtls findByEmailAddress(String emailAddress);

	//public List<TeacherDtls> findByApprovedAndRegistrationDateGreaterThanEqual(boolean b, LocalDate registrationDate);

	public void save(CreateClass create);

//	public List<TeacherDtls> findByCollegeIn(List<String> collect);

	public List<TeacherDtls> findByApprovedAndCollegeIn(boolean b, List<String> collect);


}