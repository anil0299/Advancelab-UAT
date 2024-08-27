package com.Advanceelab.cdacelabAdvance.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Advanceelab.cdacelabAdvance.entity.ResetPassword;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.mailSender.EmailSenderService;
import com.Advanceelab.cdacelabAdvance.repository.ResetPasswordRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.service.ResetPasswordService;

@Controller
@RequestMapping("/resetPassword")
public class RestPasswordController {
	
	@Autowired
	private ResetPasswordRepository resetPasswordRepository;
	
	@Autowired
	private ResetPasswordService resetPasswordService;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/forgotPassword")
	public String forgotPassword()
	{
		return "forgotPassword";
	}
	
	@PostMapping("/getOTP")
	public String getOTP(@RequestParam(value = "email") String email, Model model)
	{
		String emailAddress = email.toLowerCase();
		StudentDtls studentDtls = studentRepository.findByEmailAddress(emailAddress);
		User user = userRepository.findByEmailAddress(emailAddress);
		
		if(studentDtls != null)
		{
			if(user != null)
			{
				String otp = resetPasswordService.generateOTP();
				String mailBody;
				mailBody = "Dear " + studentDtls.getFirstName() + "\nYour Reset password OTP is : " + otp ;
				emailSenderService.sendEmail(emailAddress, "Your OTP for Password Reset", mailBody);
				
				List<ResetPassword> AllReadyOTP = resetPasswordRepository.findAllByEmailAddress(emailAddress);
				for(ResetPassword RP: AllReadyOTP)
				{
					resetPasswordRepository.deleteById(RP.getId());
				}
				
				ResetPassword resetPassword = new ResetPassword();
				resetPassword.setEmailAddress(emailAddress);
				resetPassword.setOtp(otp);
				resetPasswordRepository.save(resetPassword);
				
				model.addAttribute("email", emailAddress);
	            model.addAttribute("otpMsg", "OTP sent to your registered email.");
	            return "verifyOTP";
			}
			else
			{
				model.addAttribute("msg", "You are not approved by admin kindally contact to portal admin.");
				return "forgotPassword";
			}
		}
		else
		{
			model.addAttribute("msg", "Your email address is incorrect");
			return "forgotPassword";
		}
	}
	
	@PostMapping("/verifyOTP")
	public String verifyOTP(@RequestParam(value = "email") String email, @RequestParam(value = "otp1") String otp1,
							@RequestParam(value = "otp2") String otp2, @RequestParam(value = "otp3") String otp3,
							@RequestParam(value = "otp4") String otp4, @RequestParam(value = "otp5") String otp5,
							@RequestParam(value = "otp6") String otp6, Model model)
	{
		String emailAddress = email.toLowerCase();
		String otp = otp1+otp2+otp3+otp4+otp5+otp6;
		System.out.println("otp : " + otp);
		ResetPassword resetPassword = resetPasswordRepository.findByEmailAddress(emailAddress);
		if(resetPassword != null)
		{
			if(resetPassword.getOtp().equals(otp))
			{
				resetPasswordRepository.deleteById(resetPassword.getId());
				model.addAttribute("email", emailAddress);
				return "resetPassword";
			}
			else
			{
				//resetPasswordRepository.deleteById(resetPassword.getId());
				model.addAttribute("email", emailAddress);
				model.addAttribute("msg", "OTP is not valid try again.");
				return "verifyOTP";
			}
		}
		return"redirect:/login";
	}
	
	@Transactional
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam(value = "email", required = true) String email,
								@RequestParam(value = "password", required = true) String password,
								Model model)
	{
		String encodedPassword = bCryptPasswordEncoder.encode(password);
		StudentDtls studentDtls = studentRepository.findByEmailAddress(email.toLowerCase());
		User user = userRepository.findByEmailAddress(email.toLowerCase());
		if(studentDtls == null || user == null)
		{
			model.addAttribute("msg", "Your password is not reset try again..");
			return "resetPasswordSucess";
		}
		if(studentDtls != null)
		{
			studentDtls.setPassword(encodedPassword);
			studentRepository.save(studentDtls);
		}
		
		if(user != null)
		{
			user.setPassword(encodedPassword);
			userRepository.save(user);
		}
		return "resetPasswordSucess";
	}
}
