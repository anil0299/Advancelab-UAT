
package com.Advanceelab.cdacelabAdvance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;

@SpringBootApplication
public class CdacelabAdvanceApplication extends SpringBootServletInitializer implements CommandLineRunner{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CdacelabAdvanceApplication.class, args);
		
	}
	
	@Override 
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CdacelabAdvanceApplication.class);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		User us = userRepository.findByRole1("ADMIN");
		
//		if(us == null)
//		{
//			User user = new User();
//			user.setUsername("admin@gmail.com");
//			user.setEmailAddress("admin@gmail.com");
//			user.setPassword(passwordEncoder.encode("Cd@c@123#"));
//			user.setRole("ADMIN");
//			this.userRepository.save(user);
//		}
//		
//		StudentDtls studentDtls = studentRepository.findByRole1("ADMIN");
//		
//		if(studentDtls == null)
//		{
//			StudentDtls user = new StudentDtls();
//			user.setEmailAddress("admin@gmail.com");
//			user.setPassword(passwordEncoder.encode("Cd@c@123#"));
//			user.setRole("ADMIN");
//			user.setApproved(true);
//			this.studentRepository.save(user);
//		}
	}

}