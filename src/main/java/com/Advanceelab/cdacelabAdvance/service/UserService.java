package com.Advanceelab.cdacelabAdvance.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public void saveLoginUser(String email,LocalDate validTill ,StudentDtls studentDtls)
	{
		String username = email.substring(0,email.indexOf('@'))+"cybergyan.in";
		User user = new User();
		user.setEmailAddress(email);
		user.setUsername(username);
		user.setBatch(studentDtls.getBatch());
		user.setPassword(studentDtls.getPassword());
		user.setRole(studentDtls.getRole());
		user.setApprovalDate(studentDtls.getApprovedDate());
		user.setRegistrationDate(studentDtls.getRegistrationDate());
		user.setValidTill(validTill);
		user.setApproved(true);
		user.setLoginAttempt(0);
		userRepo.save(user);
	}
}
