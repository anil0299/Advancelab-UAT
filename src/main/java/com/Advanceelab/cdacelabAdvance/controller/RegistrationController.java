package com.Advanceelab.cdacelabAdvance.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Advanceelab.cdacelabAdvance.entity.AdvanceLabUserVmDetails;
import com.Advanceelab.cdacelabAdvance.entity.Passwordhistory;
import com.Advanceelab.cdacelabAdvance.entity.RejectedStudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.StudentTrackTime;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.mailSender.EmailSenderService;
import com.Advanceelab.cdacelabAdvance.repository.AddCenterRepo;
import com.Advanceelab.cdacelabAdvance.repository.AddCoursesRepo;
import com.Advanceelab.cdacelabAdvance.repository.AdvanceLabUserVmDetailsRepository;
import com.Advanceelab.cdacelabAdvance.repository.PasswordhistoryRepository;
import com.Advanceelab.cdacelabAdvance.repository.RejectedStudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentDtlsCopyRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentTrackTimeRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.security.EncodingUtility2;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;
import com.Advanceelab.cdacelabAdvance.service.ActiveDirectoryService;
import com.Advanceelab.cdacelabAdvance.service.AdvanceLabService;
import com.Advanceelab.cdacelabAdvance.service.BasicLabService;
import com.Advanceelab.cdacelabAdvance.service.GuacamoleService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@Controller
public class RegistrationController {

	@Autowired
	private RejectedStudentRepository RejectRepo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private AddCoursesRepo addcoursesRepo;

	@Autowired
	private AddCenterRepo addcenterRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EmailSenderService senderService;
	
	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private PasswordhistoryRepository passwordRepo;
	
	@Autowired
	private GuacamoleService guacamoleService;
	
	@Autowired
	private ActiveDirectoryService activeDirectoryService;
	
	@Autowired
	private BasicLabService basicLabService;
	
	@Autowired
	private AdvanceLabService advanceLabService;

	@Autowired
	private AdvanceLabUserVmDetailsRepository advanceLabUserVmDetailsRepository;

	
	@GetMapping("/registration")
	public String register(Model model) {

		List<String> courses = addcoursesRepo.findCourse();
		List<String> states = addcenterRepo.findDistinctState();
		model.addAttribute("courses", courses);
		model.addAttribute("states", states);
		getColleges("Uttar Pradesh");
		return "register";

	}

	@GetMapping("/colleges")
	@ResponseBody
	public List<String> getColleges(@RequestParam("state") String state) {
		// System.out.println("hey hey");
		List<String> colleges = addcenterRepo.findCollegesByState(state);
		// System.out.println(colleges);
		return colleges;
	}

	@GetMapping("/checkUniqueEmail")
	@ResponseBody
	public String checkUniqueEmail(@RequestParam("email") String email) {
		
		if(!email.contains("@")) {
			return "notExists";
		}
		String username = email.substring(0,email.indexOf("@"));
		String labmail = username+"@cybergyan.in";
		boolean exists = studentRepo.existsByEmailAddress(email) || RejectRepo.existsByEmailAddress(email) || studentRepo.existsByLabemail(labmail);
		if (exists) {
			// System.out.println("exists");
			return "exists";
		} else {
			// System.out.println("not exists");
			return "notExists";
		}
	}

	@PostMapping("/rejectuser/{id}")
	public String rejectuser(@PathVariable(name = "id") int id, HttpSession session,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode ) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}
		Optional<StudentDtls> student = studentRepo.findById(id);
		List<User> list = userRepo.findAll();

		if (student.isPresent()) {
			StudentDtls studentDtls = student.get();
			String studentEmail = studentDtls.getEmailAddress();

			int atIndex = studentEmail.indexOf('@');
			String username = studentEmail.substring(0, atIndex);
			String domain = username;

			String newEmail = domain + "@cybergyan.in";

			for (User user : list) {
				if (user.getUsername().equals(newEmail)) {
					// found a matching user
					// do something with the user object
					// stop searching once a match is found
					userRepo.delete(user);
					break;
				}
			}
		}
		studentRepo.deleteById(id);
		session.setAttribute("del", "Student Deleted Successfully !!!");
		return "redirect:/StudentApproval";
	}

	@Transactional
	@GetMapping("/disableuser/{emailAddress}")
	public String disableUser(@PathVariable("emailAddress") String emailAddress, Model model, HttpSession session) throws InterruptedException, ExecutionException {
		User user = userRepo.findByEmailAddress(emailAddress);
		if (user != null) {
			// Move the student to the RejectedStudentDtls table
			Optional<StudentDtls> studentDtlsOptional = Optional
					.ofNullable(studentRepo.findByEmailAddress(emailAddress));
			if (studentDtlsOptional.isPresent()) {
				StudentDtls studentDtls = studentDtlsOptional.get();
				
				//deleting the user from active directory
				String samAccountName = studentDtls.getId()+"";
				System.out.println(samAccountName);
				activeDirectoryService.removeUserInAD(samAccountName);
				
				// Create a new instance of the RejectedStudentDtls entity
				RejectedStudentDtls rejectedStudentDtls = new RejectedStudentDtls();
				rejectedStudentDtls.setPassword(studentDtls.getPassword());
				rejectedStudentDtls.setRole(studentDtls.getRole());
				rejectedStudentDtls.setEmailAddress(studentDtls.getEmailAddress());
				rejectedStudentDtls.setRegistrationDate(studentDtls.getRegistrationDate());
				rejectedStudentDtls.setState(studentDtls.getState());
				rejectedStudentDtls.setCollege(studentDtls.getCollege());
				rejectedStudentDtls.setQualification(studentDtls.getQualification());
				rejectedStudentDtls.setGender(studentDtls.getGender());
				rejectedStudentDtls.setFirstName(studentDtls.getFirstName());
				rejectedStudentDtls.setLastName(studentDtls.getLastName());
				rejectedStudentDtls.setFatherName(studentDtls.getFatherName());
				rejectedStudentDtls.setCategory(studentDtls.getCategory());
				rejectedStudentDtls.setDob(studentDtls.getDob());
				rejectedStudentDtls.setMobileNumber(studentDtls.getMobileNumber());
				rejectedStudentDtls.setCategoryCertificate(studentDtls.getCategoryCertificate());
				rejectedStudentDtls.setLastMarksheet(studentDtls.getLastMarksheet());

				// Save the rejected student details to the RejectedStudentDtls table
				RejectRepo.save(rejectedStudentDtls);

				// Delete the student from the StudentDtls table
				studentRepo.deleteById(studentDtls.getId());
			}

			//Deleting user from Guacamole Server
			String authToken = guacamoleService.getAdminToken().get();
			String username = emailAddress.substring(0, emailAddress.indexOf('@'));
			
			List<AdvanceLabUserVmDetails> allUserVM = advanceLabUserVmDetailsRepository.findByUsername(user.getUsername());
			for(AdvanceLabUserVmDetails vm : allUserVM) {
				String connectionIdentifier = guacamoleService.getConnectionIdentifier(vm.getVmName(), authToken).get();
				boolean status = guacamoleService.deleteConnection(connectionIdentifier, authToken).get();
				System.out.println("Connection deleted: " + status);
				advanceLabUserVmDetailsRepository.delete(vm);
				advanceLabService.deleteVm(vm.getUUID());
			}
			guacamoleService.deleteUser(username, authToken);
			
			// Delete the user from the user table
			userRepo.deleteByEmailAddress(emailAddress);
			
		}
		session.setAttribute("access", "Student Access Disabled Successfully !!!");
		return "redirect:/ManageCourseAccess";
	}


	@GetMapping("/ManageCourseAccess")
	public ModelAndView manageCourseAccess() {
		ModelAndView modelAndView = new ModelAndView();

		LocalDate currentDate = LocalDate.now(); // Get the current date

		List<User> userList = userRepo.findAll();

		// Filter users based on valid range and role
		List<User> validUsers = userList.stream().filter(user -> user.getRole().equals("USER")) // Filter by role
				.filter(user ->
//	                user.getApprovalDate().isBefore(currentDate) && // Valid from should be before current date
				user.getValidTill().isAfter(currentDate) // Valid till should be after current date
				).collect(Collectors.toList());

		modelAndView.addObject("userRoles", validUsers);
		modelAndView.setViewName("ManageCourseAccess");
		return modelAndView;
	}



	@PostMapping("/filter")
	public ModelAndView filter(@RequestParam("filteredData") String filteredData,
			@RequestParam("_csrf") String csrfToken, HttpServletRequest request, ModelAndView mav) {
		// Process the filtered data as needed (e.g., save to the database or perform
		// additional operations)
		// For simplicity, we will directly pass it to the view.
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);

		if (csrf.getToken().equals(csrfToken)) {
//		ModelAndView modelAndView = new ModelAndView("filter");
			mav.setViewName("filter");
			mav.addObject("filteredData", filteredData);
		} else {
			mav.setViewName("error.html");
		}
		// System.out.println("filteredData");
		return mav;
	}

	@PostMapping("/update/{id}")
	public String update(@PathVariable(value = "id") int id, Model model, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}
		
		Optional<StudentDtls> userDtls = studentRepo.findById(id);
		StudentDtls user = userDtls.get();
		List<String> courses = addcoursesRepo.findCourse();
		List<String> states = addcenterRepo.findDistinctState();
		String state = user.getState(); // Get the state value from the user object
		List<String> colleges = addcenterRepo.findCollegesByState(state);
		model.addAttribute("colleges", colleges);
		model.addAttribute("courses", courses);
		model.addAttribute("states", states);
		model.addAttribute("student", user);
		return "updateByAdmin";
	}
	
	@PostMapping("/updateUser")
	public String updateUser(HttpSession session, @RequestParam("id") int id, @RequestParam("state") String state,
			@RequestParam("college") String college, @RequestParam("qualification") String qualification,
			@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("fatherName") String fatherName, @RequestParam("category") String category,
			@RequestParam(value = "dob", required = false) String dobStr,
			@RequestParam("mobileNumber") String mobileNumber, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request, @RequestParam(value="hppCode") String hppCode, @RequestParam(value = "categoryCertificate", required = false) MultipartFile categoryCertificate,
	        @RequestParam(value = "lastMarksheet", required = false) MultipartFile lastMarksheet,Model model, RedirectAttributes redirectAttributes) throws IOException {

		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);

		if (csrf.getToken().equals(csrfToken)) {
			StudentDtls student = studentRepo.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));
			
			
			String[] params = new String[] { state, college,qualification,firstName,lastName,fatherName,category,mobileNumber };
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			} 

			
			student.setState(state.replaceAll("\\<.*?\\>", ""));
			student.setCollege(college.replaceAll("\\<.*?\\>", ""));
			student.setQualification(qualification.replaceAll("\\<.*?\\>", ""));
			student.setFirstName(firstName.replaceAll("\\<.*?\\>", ""));
			student.setLastName(lastName.replaceAll("\\<.*?\\>", ""));
			student.setFatherName(fatherName.replaceAll("\\<.*?\\>", ""));
			student.setCategory(category.replaceAll("\\<.*?\\>", ""));
			student.setMobileNumber(mobileNumber.replaceAll("\\<.*?\\>", ""));

			// Validate and parse the date of birth if provided
			if (dobStr != null && !dobStr.isEmpty()) {
				LocalDate dob = null;
				try {
					dob = LocalDate.parse(dobStr);
				} catch (DateTimeParseException ex) {
					// Handle invalid date
					throw new IllegalArgumentException("Invalid date format: " + dobStr);
				}
				student.setDob(dob);
			}
			
			// Handle category certificate file
		    if (categoryCertificate != null && !categoryCertificate.isEmpty()) {
		        // Process the category certificate PDF
		        try (PDDocument pdfDocument = PDDocument.load(categoryCertificate.getInputStream())) {
		            // Validate PDF content
		            PDFTextStripper textStripper = new PDFTextStripper();
		            String pdfContent = textStripper.getText(pdfDocument);
		            if (pdfContent.contains("<script>")) {
		                redirectAttributes.addFlashAttribute("msg4", "CategoryCertificate is malicious.");
		                return "redirect:/StudentApproval";
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		            return "redirect:/StudentApproval";
		        }
		        // Set the category certificate byte array
		        student.setCategoryCertificate(categoryCertificate.getBytes());
		    }

		    // Handle last marksheet file
		    if (lastMarksheet != null && !lastMarksheet.isEmpty()) {
		        // Process the last marksheet PDF
		        try (PDDocument pdfDocument = PDDocument.load(lastMarksheet.getInputStream())) {
		            // Validate PDF content
		            PDFTextStripper textStripper = new PDFTextStripper();
		            String pdfContent = textStripper.getText(pdfDocument);
		            if (pdfContent.contains("<script>")) {
		                redirectAttributes.addFlashAttribute("msg5", "LastMarksheet is malicious.");
		                return "redirect:/StudentApproval";
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		            return "redirect:/StudentApproval";
		        }
		        // Set the last marksheet byte array
		        student.setLastMarksheet(lastMarksheet.getBytes());
		    }


			studentRepo.save(student);
			session.setAttribute("update", "Student Details Updated Successfully !!!");
			return "redirect:/StudentApproval";
		}

		return "error.html";
	}

	@PostMapping("/rejected/{studentDtlsId}")
	public String rejectStudentDetails(@PathVariable int studentDtlsId, Model model, HttpSession session, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(studentDtlsId);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}
		// Retrieve the student details to be rejected
		StudentDtls rejectedStudent = studentRepo.findById(studentDtlsId).orElseThrow();

		// Create a new instance of the rejected student details entity
		RejectedStudentDtls rejectedStudentDtls = new RejectedStudentDtls();
		rejectedStudentDtls.setId(rejectedStudent.getId());
		rejectedStudentDtls.setState(rejectedStudent.getState());
		rejectedStudentDtls.setCollege(rejectedStudent.getCollege());
		rejectedStudentDtls.setQualification(rejectedStudent.getQualification());
		rejectedStudentDtls.setGender(rejectedStudent.getGender());
		rejectedStudentDtls.setFirstName(rejectedStudent.getFirstName());
		rejectedStudentDtls.setLastName(rejectedStudent.getLastName());
		rejectedStudentDtls.setFatherName(rejectedStudent.getFatherName());
		rejectedStudentDtls.setCategory(rejectedStudent.getCategory());
		rejectedStudentDtls.setDob(rejectedStudent.getDob());
		rejectedStudentDtls.setEmailAddress(rejectedStudent.getEmailAddress());
		rejectedStudentDtls.setMobileNumber(rejectedStudent.getMobileNumber());
		rejectedStudentDtls.setRole("USER");
		rejectedStudentDtls.setPassword(rejectedStudent.getPassword());
		rejectedStudentDtls.setCategoryCertificate(rejectedStudent.getCategoryCertificate());
		rejectedStudentDtls.setLastMarksheet(rejectedStudent.getLastMarksheet());
		rejectedStudentDtls.setRegistrationDate(rejectedStudent.getRegistrationDate());
		

		// Save the rejected student details to the database
		RejectRepo.save(rejectedStudentDtls);

		List<RejectedStudentDtls> newrejectedStudentDtls = RejectRepo.findAll();
		model.addAttribute("newrejectedStudentDtls", newrejectedStudentDtls);

		studentRepo.deleteById(studentDtlsId);
		
		String rejectionEmail = "Dear Participant,\n\n" +
		        "Thank you for showing your interest in the course \"Ethical Hacking and Penetration Testing\".\n\n" +
		        "We are sorry to inform you that your registration request for the course has been rejected due to any of the following reasons:\n\n" +
		        "1. You are not eligible for the course.\n" +
		        "2. The documents submitted by you are incorrect.\n\n" +
		        "For any query, please send an email to at cybergyan-noida@cdac.in.\n\n" +
		        "Thanks & Regards,\n" +
		        "CyberGyan Team";

		senderService.sendEmail(rejectedStudentDtls.getEmailAddress(), "Cyber Gyan", rejectionEmail);

		
		session.setAttribute("reject", "Student Rejected Successfully !!!");		
		return "redirect:/StudentApproval";
		
	}

//	@GetMapping("/StudentApproval")
//	public String getStudentDtls(Model model,@RequestParam(name = "error", required = false) String error) {
//		
//		if(error != null)
//			model.addAttribute("error", "Email or username already exists..");
//		
//		List<StudentDtls> studentDtls = studentRepo.findByApproved(false);
//		model.addAttribute("studentDtls", studentDtls);
//
//		List<StudentDtls> list = studentRepo.listofStudentByRole("USER", true);
//		model.addAttribute("studentDtls1", list);
//
//		List<RejectedStudentDtls> newrejectedStudentDtls = RejectRepo.findAll();
//		model.addAttribute("newrejectedStudentDtls", newrejectedStudentDtls);
//
//		return "StudentApproval";
//	}
	
	
	@GetMapping("/StudentApproval")
	public String getStudentDtls(Model model,
	                             @RequestParam(defaultValue = "0") int currentPageNo,
	                             @RequestParam(defaultValue = "25") int currentPageSize,
	                             @RequestParam(defaultValue = "0") int approvedCurrentPageNo,
	                             @RequestParam(defaultValue = "25") int approvedPageSize,
	                             @RequestParam(defaultValue = "0") int rejectedCurrentPageNo,
	                             @RequestParam(defaultValue = "25") int rejectedCurrentPageSize,
	                             @RequestParam(required = false) String searchEmailPending,
	                             @RequestParam(required = false) String searchEmailApproved,
	                             @RequestParam(required = false) String searchEmailRejected) {
	    
	    Pageable pageable = PageRequest.of(currentPageNo, currentPageSize, Sort.by("id").ascending());
	    //Page<StudentDtls> pagePost = studentRepo.findByApproved(false,pageable);
	    Page<StudentDtls> pagePost;
	    long totalPendingStudents = 0;
	    int tmp = 0;
	    if (searchEmailPending != null && !searchEmailPending.isEmpty()) {
	        pagePost = studentRepo.findByEmailAddressContainingIgnoreCaseAndApproved(searchEmailPending, false, pageable);
	        tmp = 1;
	    } else {
	        pagePost = studentRepo.findByApproved(false, pageable);
	        totalPendingStudents = pagePost.getTotalElements();
	    }
	    List<StudentDtls> studentDtls = pagePost.getContent();
	    if(tmp == 1)
	    	totalPendingStudents = studentRepo.findByApproved(false,pageable).getTotalElements();
	    
	    
	    Pageable pageable1 = PageRequest.of(approvedCurrentPageNo, approvedPageSize, Sort.by("id").ascending());
	    //Page<StudentDtls> pagePost1 = studentRepo.findByApprovedAndRole(true,"USER",pageable1);
	    Page<StudentDtls> pagePost1;
	    long totalApprovedStudents = 0;
	    int tmp1 = 0;
	    if(searchEmailApproved != null && !searchEmailApproved.isEmpty()) {
	    	pagePost1 = studentRepo.findByEmailAddressContainingIgnoreCaseAndApprovedAndRole(searchEmailApproved, true,"USER",pageable1);
	    	tmp1 = 1;
	    }else {
	    	pagePost1 = studentRepo.findByApprovedAndRole(true,"USER",pageable1);
	    	totalApprovedStudents = pagePost1.getTotalElements();
	    }
	    List<StudentDtls> studentDtls1 = pagePost1.getContent();
	    if(tmp1 == 1)
	    	totalApprovedStudents = studentRepo.findByApprovedAndRole(true,"USER",pageable1).getTotalElements();
	    
	    
	    Pageable pageable2 = PageRequest.of(rejectedCurrentPageNo, rejectedCurrentPageSize, Sort.by("id").ascending());
	    //Page<RejectedStudentDtls> pagePost2 = RejectRepo.findAll(pageable2);
	    Page<RejectedStudentDtls> pagePost2;
	    long totalRejectedStudents = 0;
	    int tmp2 = 0;
	    if(searchEmailRejected != null && !searchEmailRejected.isEmpty()) {
	    	pagePost2 = RejectRepo.findByEmailAddress(searchEmailRejected,pageable2);
	    	tmp2 = 1;
	    }else {
	    	pagePost2 = RejectRepo.findAll(pageable2);
	    	totalRejectedStudents = pagePost2.getTotalElements();
	    }
	    List<RejectedStudentDtls> studentDtls2 = pagePost2.getContent();
	    if(tmp2 == 1)
	    	totalRejectedStudents =  RejectRepo.findAll(pageable2).getTotalElements();
	    
	    model.addAttribute("studentDtls", studentDtls);
	    model.addAttribute("totalPendingStudents", totalPendingStudents);
	    
	    model.addAttribute("studentDtls1", studentDtls1);
	    model.addAttribute("totalApprovedStudents", totalApprovedStudents);
	    
	    model.addAttribute("newrejectedStudentDtls", studentDtls2);
	    model.addAttribute("totalRejectedStudents", totalRejectedStudents);
	    
	    model.addAttribute("currentPage", currentPageNo);
	    model.addAttribute("totalPages", pagePost.getTotalPages());
	    model.addAttribute("currentPageSize", currentPageSize);
	    
	    model.addAttribute("approvedCurrentPageNo", approvedCurrentPageNo);
	    model.addAttribute("approvedtotalPages", pagePost1.getTotalPages());
	    model.addAttribute("approvedPageSize", approvedPageSize);
	    
	    model.addAttribute("rejectedCurrentPageNo", rejectedCurrentPageNo);
	    model.addAttribute("rejectedtotalPages", pagePost2.getTotalPages());
	    model.addAttribute("rejectedCurrentPageSize", rejectedCurrentPageSize);
	 
	    return "StudentApproval";
	}
	

	@PostMapping("/rejectedDeleted/{id}")
	public String rejectedDelete(@PathVariable(name = "id") int id, HttpSession session, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}
		RejectRepo.deleteById(id);
		session.setAttribute("pdelete", "Student Permanent Deleted Successfully !!!");
		return "redirect:/StudentApproval";
	}

	@PostMapping("/StudentApproval/approve-studentDtls/{studentDtlsId}")
	@Transactional
	public String approveStudentDtls(@PathVariable int studentDtlsId, HttpSession session, Model model,RedirectAttributes redirectAttributes,
			@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(studentDtlsId);
			String[] params = new String[] { a };
					
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			}
			Optional<StudentDtls> studentDtlsOptional = studentRepo.findById(studentDtlsId);
			if (studentDtlsOptional.isPresent()) {
				StudentDtls studentDtls = studentDtlsOptional.get();
				String emailAddress = studentDtls.getEmailAddress();
				String password = studentDtls.getPassword();
				if (userRepo.existsByUsernameOrEmailAddress(emailAddress, emailAddress)) {
					redirectAttributes.addFlashAttribute("error", "Email or username already exists"); 
					List<RejectedStudentDtls> newRejectedStudentDtls = RejectRepo.findAll();
					model.addAttribute("newrejectedStudentDtls", newRejectedStudentDtls);
					return "redirect:/StudentApproval";
				}

				int atIndex = emailAddress.indexOf('@');
				String username = emailAddress.substring(0, atIndex);
				String newEmail = username + "@cybergyan.in";
				User user = new User();
				user.setUsername(newEmail);
				user.setPassword(password);
				user.setRole("USER");
				user.setEmailAddress(emailAddress);
				user.setApproved(true);
				user.setLoginAttempt(0);
				Optional<StudentDtls> studentDtlsOptional1 = studentRepo.findById(studentDtlsId);
				if (studentDtlsOptional1.isPresent()) {
					StudentDtls studentDtls1 = studentDtlsOptional1.get();
					LocalDate registrationDate1 = studentDtls1.getRegistrationDate();
					user.setRegistrationDate(registrationDate1);
					LocalDate validTill = LocalDate.now().plusDays(90);
					user.setValidTill(validTill);
					LocalDate approvedDate = LocalDate.now();
					user.setApprovalDate(approvedDate);
				}
				userRepo.save(user);

		        Passwordhistory phistory = new Passwordhistory();
		        phistory.setId(studentDtlsId);
		        phistory.setEmailAddress(emailAddress);
		        phistory.setPassword(password);
		        phistory.setRegistrationDate(LocalDateTime.now());       
		        passwordRepo.save(phistory);
				studentDtls.setApproved(true);
				studentDtls.setClassName("");
				LocalDate validTill = LocalDate.now().plusDays(90);
				studentDtls.setValidTill(validTill);
				LocalDate approvedDate = LocalDate.now();
				studentDtls.setApprovedDate(approvedDate);
				studentRepo.save(studentDtls);

				//Creating account in AD-Cybergyan
				
				String firstName = studentDtls.getFirstName();
				String lastName = studentDtls.getLastName();
				LocalDate dob = studentDtls.getDob();
				String hciPassword = basicLabService.generateHciPassword(firstName, dob);
				String samAccountName = studentDtls.getId()+"";
				String output = activeDirectoryService.createUserInAD(firstName+" "+lastName, hciPassword, samAccountName, newEmail, username);
				System.out.println(output);
				
				String confirmationEmail = "Confirmation Email:\n\n" +
				        "Greetings from Cyber Gyan Team!\n\n" +
				        "Your registration to the Cyber Security Programme on Ethical Hacking and Penetration Testing has been successfully completed!\n\n" +
				        "Please access your course content using the following URL: https://cdaccybergyan.uat.dcservices.in/welcome\n\n" +
				        "We have also created a dedicated WhatsApp group exclusively for registered participants. This group will serve as a platform for all updates, notifications, doubt clearing sessions, and certification-related information for the training program.\n\n" +
				        "Join the WhatsApp group using the following link: https://chat.whatsapp.com/JbFMvLM6tARFsUL6LIH6WG\n\n" +
				        "Happy Learning! 😊\n\n" +
				        "Best Regards,\n" +
				        "CyberGyan Team";

				senderService.sendEmail(emailAddress, "Confirmation of Registration - Virtual Internship Programme on Cyber Security", confirmationEmail);


			}

			List<RejectedStudentDtls> newRejectedStudentDtls = RejectRepo.findAll();
			model.addAttribute("newrejectedStudentDtls", newRejectedStudentDtls);

			session.setAttribute("msg", "Student Approved Successfully !!!");
			return "redirect:/StudentApproval";
		} else {
			return "error.html";
		}
	}

	@PostMapping("/extend-validity/{studentDtlsId}")
	public String extendValidity(@PathVariable("studentDtlsId") int studentDtlsId,
			@RequestParam(value = "validTill") String dobStr,
			HttpSession session, Model model, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,
			@RequestParam(value="hppCode") String hppCode) {

		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);

		if (csrf.getToken().equals(csrfToken)) {
			Optional<StudentDtls> studentDtlsOptional = studentRepo.findById(studentDtlsId);
			
				String[] params = new String[] { dobStr };
			
			
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
				return "error.html";
			} 
			
			LocalDate dob = null;
			try {
				dob = LocalDate.parse(dobStr);
			} catch (DateTimeParseException ex) {
				// handle invalid date
				throw new IllegalArgumentException("Invalid date format: " + dobStr);
			}
			
			
			
			if (studentDtlsOptional.isPresent()) {
				StudentDtls studentDtls = studentDtlsOptional.get();

				// Update the student's validity date
				studentDtls.setValidTill(dob);
				studentRepo.save(studentDtls);

				// Update the associated User entity's validity date if user is not null
				User user1 = userRepo.findByEmailAddress(studentDtls.getEmailAddress());
				user1.setValidTill(dob);
				userRepo.save(user1);
			}

			session.setAttribute("extend", "Validity Extended Successfully !!!");
			return "redirect:/StudentApproval";
		} else {
			return "error.html";
		}
	}

	@PostMapping("/RejectedApproval/{id}")
	public String rejectedApprove(@PathVariable(value = "id") int id, HttpSession session, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}
		Optional<RejectedStudentDtls> rejectedStudent = RejectRepo.findById(id);
		RejectedStudentDtls rejected = rejectedStudent.get();

		if (rejectedStudent.isPresent()) {
			
			String confirmationEmail = "Confirmation Email:\n\n" +
			        "Greetings from Cyber Gyan Team!\n\n" +
			        "Your registration to the Cyber Security Programme on Ethical Hacking and Penetration Testing has been successfully completed!\n\n" +
			        "Please access your course content using the following URL: https://cdaccybergyan.uat.dcservices.in/welcome\n\n" +
			        "We have also created a dedicated WhatsApp group exclusively for registered participants. This group will serve as a platform for all updates, notifications, doubt clearing sessions, and certification-related information for the training program.\n\n" +
			        "Join the WhatsApp group using the following link: https://chat.whatsapp.com/D7aUuM3PM1bDwoCyJdev4X\n\n" +
			        "Happy Learning! 😊\n\n" +
			        "Best Regards,\n" +
			        "CyberGyan Team";

			senderService.sendEmail(rejected.getEmailAddress(), "Cyber Gyan", confirmationEmail);
			
		}
		StudentDtls studentDtls1 = new StudentDtls();

		studentDtls1.setApproved(true);
		studentDtls1.setState(rejected.getState());
		studentDtls1.setCollege(rejected.getCollege());
		studentDtls1.setQualification(rejected.getQualification());
		studentDtls1.setGender(rejected.getGender());
		studentDtls1.setFirstName(rejected.getFirstName());
		studentDtls1.setLastName(rejected.getLastName());
		studentDtls1.setFatherName(rejected.getFatherName());
		studentDtls1.setCategory(rejected.getCategory());
		studentDtls1.setDob(rejected.getDob());
		studentDtls1.setEmailAddress(rejected.getEmailAddress());
		studentDtls1.setRole("USER");
		studentDtls1.setClassName("");
		studentDtls1.setMobileNumber(rejected.getMobileNumber());
		studentDtls1.setPassword(rejected.getPassword());
		studentDtls1.setCategoryCertificate(rejected.getCategoryCertificate());
		studentDtls1.setLastMarksheet(rejected.getLastMarksheet());
		studentDtls1.setRegistrationDate(rejected.getRegistrationDate());
		LocalDate validTill = LocalDate.now().plusDays(90);
		studentDtls1.setValidTill(validTill);
		LocalDate approvedDate = LocalDate.now();
    	studentDtls1.setApprovedDate(approvedDate);
    
    	
    	// Generate a new email or username
		int atIndex = rejected.getEmailAddress().indexOf('@');
		String username = rejected.getEmailAddress().substring(0, atIndex);
		String domain = rejected.getEmailAddress().substring(atIndex + 1);
		/*
		 * if (domain.equals("gmail.com")) { username = username.replace(".", ""); }
		 */
		String newEmail = username + "@cybergyan.in";
		studentDtls1.setLabemail(newEmail);
		studentRepo.save(studentDtls1);		
		
		//creating object for lab login 
		User user=new User();
		user.setEmailAddress(rejected.getEmailAddress());
		user.setUsername(newEmail);
		user.setPassword(rejected.getPassword());
		user.setRole("USER");
		user.setRegistrationDate(rejected.getRegistrationDate());
		user.setValidTill(validTill);
		user.setApprovalDate(approvedDate);
		user.setApproved(true);
		user.setLoginAttempt(0);
		userRepo.save(user);
		RejectRepo.deleteById(id);
		
		//Creating account in AD-Cybergyan
		
		StudentDtls studentDtls = studentRepo.findByEmailAddress(rejected.getEmailAddress());
		String firstName = studentDtls.getFirstName();
		String lastName = studentDtls.getLastName();
		LocalDate dob = studentDtls.getDob();
		String hciPassword = basicLabService.generateHciPassword(firstName, dob);
		String samAccountName = studentDtls.getId()+"";
		String output = activeDirectoryService.createUserInAD(firstName+" "+lastName, hciPassword, samAccountName, newEmail, username);
		System.out.println(output);
		
		
		session.setAttribute("reapprove", "Student Reapprove Successfully !!!");
		return "redirect:/StudentApproval";
	}

	@PostMapping("/createUser")
	public String createuser(HttpSession session, @RequestParam("state") String state,
			@RequestParam("college") String college, @RequestParam("qualification") String qualification,
			@RequestParam("gender") String gender, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("fatherName") String fatherName,
			@RequestParam("category") String category, @RequestParam(value = "dob", required = false) String dobStr,
			@RequestParam("emailAddress") String emailAddress, @RequestParam("mobileNumber") String mobileNumber,
			@RequestParam("password") String password, @RequestParam("categoryCertificate") MultipartFile file,
			@RequestParam("lastMarksheet") MultipartFile file1, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request,@RequestParam(value="hppCode") String hppCode,Model model) throws IOException {
		
		emailAddress = emailAddress.toLowerCase();
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		
		if (csrf.getToken().equals(csrfToken)) {
		
			try (PDDocument pdfDocument = PDDocument.load(file.getInputStream())) {
				PDFTextStripper textStripper = new PDFTextStripper();
				String pdfContent = textStripper.getText(pdfDocument);

				if (pdfContent.contains("<script>")) {
					session.setAttribute("msg4", "CategoryCertificate is malicious.");
					return "redirect:/registration";
				}
			} catch (Exception e) {
				// Handle exceptions (e.g., PDF processing errors)
				e.printStackTrace();
				// Add an error message to the model if needed
				return "redirect:/registration";
			}

			try (PDDocument pdfDocument = PDDocument.load(file1.getInputStream())) {
				PDFTextStripper textStripper = new PDFTextStripper();
				String pdfContent = textStripper.getText(pdfDocument);

				if (pdfContent.contains("<script>")) {
					session.setAttribute("msg5", "LastMarksheet is malicious.");
					return "redirect:/registration";
				}
			} catch (Exception e) {
				// Handle exceptions (e.g., PDF processing errors)
				e.printStackTrace();
				// Add an error message to the model if needed
				return "redirect:/registration";
			}

		
		
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
			boolean emailExists = studentRepo.existsByEmailAddress(emailAddress)
					|| RejectRepo.existsByEmailAddress(emailAddress);
			if (emailExists) {
				session.setAttribute("sameEmailMsg", "A student with the Email " + emailAddress + " already exists.");
				return "redirect:/registration";
			}
			int seed=5;
	        password=EncodingUtility2.decodeWithSeedNEW(password, seed);
	        //System.out.println("after decode password-----------"+password);
			String[] params = new String[] { state, college,firstName,lastName,fatherName,mobileNumber,emailAddress,category,qualification,gender,dobStr,password,file.getOriginalFilename(),file1.getOriginalFilename() };
			
			
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
				return "error.html";
			} 
			
			
			byte[] pdffile = file.getBytes();
			byte[] pdffile1 = file1.getBytes();

			
			StudentDtls student = new StudentDtls();
			
		
			
			student.setState(state.replaceAll("\\<.*?\\>", ""));
			student.setCollege(college.replaceAll("\\<.*?\\>", ""));
			
			student.setQualification(qualification.replaceAll("\\<.*?\\>", ""));
			student.setGender(gender.replaceAll("\\<.*?\\>", ""));
			student.setFirstName(firstName.replaceAll("\\<.*?\\>", ""));
			student.setLastName(lastName.replaceAll("\\<.*?\\>", ""));
			student.setFatherName(fatherName.replaceAll("\\<.*?\\>", ""));
			student.setCategory(category.replaceAll("\\<.*?\\>", ""));

			// validate and parse the date
			LocalDate dob = null;
			try {
				dob = LocalDate.parse(dobStr);
			} catch (DateTimeParseException ex) {
				// handle invalid date
				throw new IllegalArgumentException("Invalid date format: " + dobStr);
			}

			student.setDob(dob);
			student.setEmailAddress(emailAddress.replaceAll("\\<.*?\\>", ""));
			student.setMobileNumber(mobileNumber.replaceAll("\\<.*?\\>", ""));
			
			student.setPassword(encoder.encode(password));
			student.setCategoryCertificate(pdffile);
			student.setLastMarksheet(pdffile1);
			student.setRegistrationDate(LocalDate.now()); // set the registration date to today's date
			student.setRole("USER");
			student.setClassName("");

			String labemail1 = emailAddress.substring(0, emailAddress.indexOf('@')) + '@' + "cybergyan.in";
			student.setLabemail(labemail1);		
			
			studentRepo.save(student);

			session.setAttribute("msg", "Registered Successfully !!!");
			session.setAttribute("msg1", "You will get an approval e-mail from the administrator.");

			return "redirect:/registration";
		} else {
			return "error.html";
		}

	}

	@PostMapping("/displayPDF/{id}")
	public void displayPDF(@PathVariable int id, HttpServletResponse response, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) throws IOException {
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				 response.sendRedirect("/error");
	        }
		}
		try {
			StudentDtls student = studentRepo.findById(id).orElseThrow();
			byte[] pdffile = student.getCategoryCertificate();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=file.pdf");
			response.setContentLength(pdffile.length);
			response.getOutputStream().write(pdffile);
			response.getOutputStream().flush();
		} catch (Exception e) {
			// handle the case when the student is not found
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.getWriter().write("Student not found");
			response.getWriter().flush();
		}
	}

	@PostMapping("/displayPDF1/{id}")
	public void displayPDF1(@PathVariable int id, HttpServletResponse response, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) throws IOException {
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				 response.sendRedirect("/error");
	        }
		}
		try {
			StudentDtls student = studentRepo.findById(id).orElseThrow();
			byte[] pdffile1 = student.getLastMarksheet();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=file.pdf");
			response.setContentLength(pdffile1.length);
			response.getOutputStream().write(pdffile1);
			response.getOutputStream().flush();
		} catch (Exception e) {
			// handle the case when the student is not found
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.getWriter().write("Student not found");
			response.getWriter().flush();
		}
	}

	@PostMapping("/displayPDF2/{id}")
	public void displayPDF2(@PathVariable int id, HttpServletResponse response, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) throws IOException {
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				 response.sendRedirect("/error");
	        }
		}
		try {
			RejectedStudentDtls student1 = RejectRepo.findById(id).orElseThrow();
			byte[] pdffile = student1.getCategoryCertificate();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=file.pdf");
			response.setContentLength(pdffile.length);
			response.getOutputStream().write(pdffile);
			response.getOutputStream().flush();
		} catch (Exception e) {
			// handle the case when the student is not found
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.getWriter().write("Student not found");
			response.getWriter().flush();
		}
	}

	@PostMapping("/displayPDF3/{id}")
	public void displayPDF3(@PathVariable int id, HttpServletResponse response, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) throws IOException {
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				 response.sendRedirect("/error");
	        }
		}
		try {
			RejectedStudentDtls student1 = RejectRepo.findById(id).orElseThrow();
			byte[] pdffile1 = student1.getLastMarksheet();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=file.pdf");
			response.setContentLength(pdffile1.length);
			response.getOutputStream().write(pdffile1);
			response.getOutputStream().flush();
		} catch (Exception e) {
			// handle the case when the student is not found
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.getWriter().write("Student not found");
			response.getWriter().flush();
		}
	}

}
