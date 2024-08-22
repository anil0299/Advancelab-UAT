package com.Advanceelab.cdacelabAdvance.service;

import java.util.List;
import java.util.Optional;

import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;





public interface StudentService {
	
	public StudentDtls createUser(StudentDtls user);

	public boolean checkEmailAddress(String emailAddress);

	public List<StudentDtls> findByApproved(boolean b);

	public List<StudentDtls> findAll();

	public Optional<StudentDtls> findById(int studentDtlsId);

	public void save(StudentDtls student);

	

	

	 

//	public List<StudentDtls> findAll();
//
//	public void deleteById(int id);
//
//	public List<StudentDtls> getPendingStudentDtls();
//
//	public StudentDtls approveStudentDtls(int studentDtlsId);
//
//	public StudentDtls rejectStudentDtls(int studentDtlsId);

	
	
	
}
