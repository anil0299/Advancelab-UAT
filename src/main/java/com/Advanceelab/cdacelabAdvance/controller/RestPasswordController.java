package com.Advanceelab.cdacelabAdvance.controller;

import java.util.Comparator;
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

import com.Advanceelab.cdacelabAdvance.entity.LoginAttempt;
import com.Advanceelab.cdacelabAdvance.entity.PasswordHistoryNew;
import com.Advanceelab.cdacelabAdvance.entity.ResetPassword;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.mailSender.EmailSenderService;
import com.Advanceelab.cdacelabAdvance.repository.LoginAttemptRepository;
import com.Advanceelab.cdacelabAdvance.repository.PasswordHistoryNewRepository;
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
	
	@Autowired
	private PasswordHistoryNewRepository passwordHistoryNewRepository;
	
	@Autowired
	private LoginAttemptRepository loginAttemptRepository;

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
		List<PasswordHistoryNew> oldFivePassword = passwordHistoryNewRepository.findAllByEmailAddress(email.toLowerCase());
		
		for(PasswordHistoryNew oldPassword: oldFivePassword)
		{
			if(bCryptPasswordEncoder.matches(password, oldPassword.getPassword()))
			{
				model.addAttribute("msg", "To create a new password, you cannot use the previous five passwords.");
				model.addAttribute("email", email);
				return "resetPassword";
			}
		}
		
		StudentDtls studentDtls = studentRepository.findByEmailAddress(email.toLowerCase());
		User user = userRepository.findByEmailAddress(email.toLowerCase());
		String AccountLockMsg = null;
		if(studentDtls == null || user == null)
		{
			model.addAttribute("msg", "Your password is not reset try again..");
			return "forgotPassword";
		}
		if(studentDtls != null)
		{
			studentDtls.setPassword(encodedPassword);
			if(!studentDtls.isActive())
			{
				LoginAttempt loginAttempt = loginAttemptRepository.findByUserId(studentDtls.getId());
				if(loginAttempt != null)
				{
					loginAttempt.setLoginAttempts(0);
					loginAttempt.setFailedAttempts(0);
					loginAttemptRepository.save(loginAttempt);
				}
				studentDtls.setActive(true);
				AccountLockMsg = "deactiveted";
			}
			studentRepository.save(studentDtls);
		}
		
		if(user != null)
		{
			user.setPassword(encodedPassword);
			if(!user.isApproved())
			{
				user.setLoginAttempt(0);
				user.setApproved(true);
				AccountLockMsg = "deactiveted";
			}
			userRepository.save(user);
		}
		
		if (oldFivePassword.size() >= 5) {
	        PasswordHistoryNew lowestIdPassword = oldFivePassword.stream()
	            .min(Comparator.comparingLong(PasswordHistoryNew::getId))
	            .orElse(null);

	        if (lowestIdPassword != null) {
	            passwordHistoryNewRepository.delete(lowestIdPassword);
	        }
	    }
		PasswordHistoryNew passwordHistoryNew = new PasswordHistoryNew();
		passwordHistoryNew.setEmailAddress(email.toLowerCase());
		passwordHistoryNew.setPassword(encodedPassword);
		passwordHistoryNewRepository.save(passwordHistoryNew);
		
		
		if(AccountLockMsg != null && AccountLockMsg.equals("deactiveted"))
		{
			model.addAttribute("msg", "Your account has also been activated.");
			emailSenderService.sendEmail(email, "Account activated", "Dear " + studentDtls.getFirstName() + "\n\nYour account has been activated.\n\n" + "Regards,\n" + "Team Cyber Gyan");
		}
		
		return "resetPasswordSucess";
	}
	
	@GetMapping("/reset")
	public String resetPassword() {
		List<PasswordHistoryNew> oldPassword = passwordHistoryNewRepository.findAllByEmailAddress("thelocaljobseekers@gmail.com");
		for(PasswordHistoryNew phn:oldPassword)
			System.out.println(phn);
		return "resetPassword";
	}
}
