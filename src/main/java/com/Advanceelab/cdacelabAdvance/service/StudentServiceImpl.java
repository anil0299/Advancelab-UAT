package com.Advanceelab.cdacelabAdvance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;




@Service
public class StudentServiceImpl implements StudentService{
    
	@Autowired
	private StudentRepository studentRepo;
	
	
	@Override
	public StudentDtls createUser(StudentDtls user) {
		
		return studentRepo.save(user);
	}

	@Override
	public boolean checkEmailAddress(String emailAddress) {
		
		
		return studentRepo.existsByEmailAddress(emailAddress);
	}

	@Override
	public String toString() {
		return "UserServiceImpl [studentRepo=" + studentRepo + "]";
	}

	@Override
	public List<StudentDtls> findByApproved(boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StudentDtls> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<StudentDtls> findById(int studentDtlsId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public void save(StudentDtls student) {
		// TODO Auto-generated method stub
		
	}

	

//	@Override
//	public List<StudentDtls> findAll() {
//		// TODO Auto-generated method stub
//		return studentRepo.findAll();
//	}
//
//	@Override
//	public void deleteById(int id) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public List<StudentDtls> getPendingStudentDtls() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public StudentDtls approveStudentDtls(int studentDtlsId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public StudentDtls rejectStudentDtls(int studentDtlsId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
   
}
