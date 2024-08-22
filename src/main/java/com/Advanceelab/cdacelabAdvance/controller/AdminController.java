package com.Advanceelab.cdacelabAdvance.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.Advanceelab.cdacelabAdvance.dto.BatchCompletionStatus;
import com.Advanceelab.cdacelabAdvance.entity.AddCourse;
import com.Advanceelab.cdacelabAdvance.entity.AdvanceLabUserVmDetails;
import com.Advanceelab.cdacelabAdvance.entity.BasicLabSubmission;
import com.Advanceelab.cdacelabAdvance.entity.CenterDetails;
import com.Advanceelab.cdacelabAdvance.entity.CourseTopics;
import com.Advanceelab.cdacelabAdvance.entity.ExerciseContent;
import com.Advanceelab.cdacelabAdvance.entity.ExerciseSubmission;
import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;
import com.Advanceelab.cdacelabAdvance.entity.Quiz;
import com.Advanceelab.cdacelabAdvance.entity.QuizUserAttempt;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.StudentTrackTime;
import com.Advanceelab.cdacelabAdvance.entity.StudentVideoWatchInf;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.entity.Video;
import com.Advanceelab.cdacelabAdvance.repository.AddCenterRepo;
import com.Advanceelab.cdacelabAdvance.repository.AddCoursesRepo;
import com.Advanceelab.cdacelabAdvance.repository.AdvanceLabUserVmDetailsRepository;
import com.Advanceelab.cdacelabAdvance.repository.BasicLabAssignRepository;
import com.Advanceelab.cdacelabAdvance.repository.Batch_Master_repo;
import com.Advanceelab.cdacelabAdvance.repository.ClassExerciseRepository;
import com.Advanceelab.cdacelabAdvance.repository.CourseTopicsRepository;
import com.Advanceelab.cdacelabAdvance.repository.ExerciseContentRepository;
import com.Advanceelab.cdacelabAdvance.repository.ExerciseSubmission_Repo;
import com.Advanceelab.cdacelabAdvance.repository.Exercise_MasterRepo;
import com.Advanceelab.cdacelabAdvance.repository.QuizRepo;
import com.Advanceelab.cdacelabAdvance.repository.QuizUserAttemptRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentTrackTimeRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentVideoWatchInfRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.repository.VideoRepository;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;
import com.Advanceelab.cdacelabAdvance.service.AdvanceLabService;
import com.Advanceelab.cdacelabAdvance.service.Batch_Service;
import com.Advanceelab.cdacelabAdvance.service.ExerciseImageService;
import com.Advanceelab.cdacelabAdvance.service.GuacamoleService;


@Controller
@RequestMapping
public class AdminController {

	@Autowired
	private BasicLabAssignRepository basiclab_Repo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private Exercise_MasterRepo exer_repo;

	@Autowired
	Batch_Service batch_service;

	@Autowired
	private AddCenterRepo addcenterRepo;

	@Autowired
	private AddCoursesRepo addcourseRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private QuizRepo quizRepo;
	
	@Autowired
	private ExerciseSubmission_Repo exerciseSubmission_Repo;

	@Autowired
	private BasicLabAssignRepository basicpdfRepo;

	@Autowired
	private CourseTopicsRepository courseTopicsRepo;

	@Autowired
	private Batch_Master_repo batch_Master_repo;

	@Autowired
	private ClassExerciseRepository classExerciseRepo;
	
	@Autowired
	private QuizUserAttemptRepository quizUserAttemptRepository;
	
	@Autowired
    private StudentVideoWatchInfRepository studentVideoWatchInfRepository;
	
	@Autowired
	private ExerciseContentRepository exerciseContentRepository;
	
	@Autowired
	private ExerciseImageService exerciseImageService;
	
	@Autowired
	private StudentTrackTimeRepository studentTrackTimeRepo;
	
	@Autowired
	private Exercise_MasterRepo exercise_MasterRepo;
	
	@Autowired
	private GuacamoleService guacamoleService;
	
	@Autowired
	private AdvanceLabUserVmDetailsRepository advanceLabUserVmDetailsRepository;
	
	@Autowired
	private AdvanceLabService advanceLabService;
	
	private Logger logger = Logger.getLogger(getClass().getName());

	@GetMapping("/AdminHome")
	public ModelAndView adminhome() {
		ModelAndView modelAndView = new ModelAndView();

		LocalDate currentDate = LocalDate.now(); // Get the current date

		List<User> userList = userRepo.findAll();

		// Filter users based on valid till date and role
		List<User> expiredUsers = userList.stream().filter(user -> user.getRole().equals("USER")) // Filter by role
				.filter(user -> user.getValidTill().isBefore(currentDate)) // Filter by valid till date
				.collect(Collectors.toList());

		modelAndView.addObject("userRoles", expiredUsers);
		modelAndView.setViewName("AdminHome");
		return modelAndView;
	}

	@Transactional
	@GetMapping("/deleteuser/{emailAddress}")
	public String deleteUser(@PathVariable("emailAddress") String emailAddress, Model model, HttpSession session) {
		User user = userRepo.findByEmailAddress(emailAddress);
		if (user != null) {
			userRepo.deleteByEmailAddress(emailAddress);
		}
		session.setAttribute("access", "Student Deleted Successfully !!!");
		return "redirect:/AdminHome";
	}

	@GetMapping("/AddExercise")
	public String addExercise(Model model) {
		// modelandview.setViewName("AddExercise.html");
		model.addAttribute("ex", new Exercise_Master());
		List<CourseTopics> courseTopics = courseTopicsRepo.findAll();

		/*
		 * if (courseTopics != null) for (CourseTopics course : courseTopics)
		 * System.out.println(course);
		 */

		model.addAttribute("courseTopics", courseTopics);

		return "AddExercise";

	}

	@PostMapping("/saveExercise")
	public String saveExercise(@ModelAttribute @Valid Exercise_Master ex, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request, Model model) throws UnsupportedEncodingException {

		Optional<CourseTopics> courseTopics = courseTopicsRepo.findById(ex.getTopicId());
		String topicName = courseTopics.get().getTopicName().trim();
        String exerName = ex.getExer_name().trim();
		if(!topicName.equals(exerName))
		{
			model.addAttribute("ex", new Exercise_Master());
			List<CourseTopics> courseTopics1 = courseTopicsRepo.findAll();
			model.addAttribute("courseTopics", courseTopics1);
			model.addAttribute("msg", "Exercise Name Must be Topics Name..");
			return "AddExercise";
		}
		
		Long exercise_Master = exer_repo.findByTopicId(ex.getTopicId());
		if(exercise_Master != null)
		{
			model.addAttribute("ex", new Exercise_Master());
			List<CourseTopics> courseTopics1 = courseTopicsRepo.findAll();
			model.addAttribute("courseTopics", courseTopics1);
			model.addAttribute("msg", "Exercise All Ready added..");
			return "AddExercise";
		}
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		String a=String.valueOf(ex.getTopicId());
		 String b=a.replaceAll("\\<.*?\\>", "");
		int tpid=Integer.parseInt(b);
		ex.setTopicId(tpid);
		ex.setExer_name(ex.getExer_name().replaceAll("\\<.*?\\>", ""));
		ex.setProb_statement(ex.getProb_statement().replaceAll("\\<.*?\\>", ""));
		
		String[] params = new String[] { ex.getExer_name(), ex.getProb_statement(),a };	
		String hppCode = ex.getHppCode();

		if (csrf.getToken().equals(csrfToken)) {
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
				return "error.html";
			} else {
				exer_repo.save(ex);
				return "alert.html";
			}
		}
		return "error.html";
	}

	
	@PostMapping("/updateEx")
	public String updateEx(@ModelAttribute @Valid Exercise_Master ex, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request) throws UnsupportedEncodingException {
		// Retrieve the CSRF token from the request parameters.
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		String a=String.valueOf(ex.getTopicId());
		 String b=a.replaceAll("\\<.*?\\>", "");
		int tpid=Integer.parseInt(b);
		
		String ex_id=String.valueOf(ex.getEx_id());
		 String new_ex_id=ex_id.replaceAll("\\<.*?\\>", "");
		
		ex.setTopicId(tpid);
		ex.setExer_name(ex.getExer_name().replaceAll("\\<.*?\\>", ""));
		ex.setProb_statement(ex.getProb_statement().replaceAll("\\<.*?\\>", ""));
		

		String[] params = new String[] { ex.getExer_name(), ex.getProb_statement(),a ,ex_id };
		
		String hppCode = ex.getHppCode();

		// Validate the CSRF token
		if (csrf.getToken().equals(csrfToken)) {

			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			} else {
				
				System.out.println(ex.getTopicId());
				exer_repo.save(ex);
				return "alert5.html";
			}

		}
		// If the CSRF token is not valid, return an error page.
		return "error.html";
	}
		
	
	@GetMapping("/exercisedetails")
	public String listexercises(Model model) {
		List<Exercise_Master> listexercise = exer_repo.findAll();
		model.addAttribute("listexercise", listexercise);
		return "exercisedetails";
	}

	// delete exercise
	@PostMapping("/deleteexer/{Ex_id}")
	public String deleteexercise(@PathVariable(name = "Ex_id") long Ex_id,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(Ex_id);
			String[] params = new String[] { a };
					
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            logger.info("HTTP Parameter pollution");
	            // Render the error page and include it in the response
	            return "error.html";
	        }
		}
		exer_repo.deleteById(Ex_id);
		return "redirect:/exercisedetails";
	}

	@GetMapping("/showupdateexer")
	public ModelAndView showupdateexer(@RequestParam Long ex_id) {
		ModelAndView mav = new ModelAndView("exerciseupdate");
		List<CourseTopics> courseTopics = courseTopicsRepo.findAll();

		/*
		 * if (courseTopics != null) for (CourseTopics course : courseTopics)
		 * System.out.println(course);
		 */

		mav.addObject("courseTopics", courseTopics);
		try {
			Optional<Exercise_Master> ex_update = exer_repo.findById(ex_id);
			if (ex_update != null) {
				mav.addObject("ex_update", ex_update);
			} else {
				throw new Exception("Unable to find object by given id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	@GetMapping("/list")
	public String listFiles(Model model) {
		File folder = new File("./pdfs");
		File[] fileList = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".pdf");
			}
		});
		List<String> pdfNames = Arrays.stream(fileList).map(file -> file.getName()).collect(Collectors.toList());

		// Add the list to the model and return the Thymleaf template name
		model.addAttribute("pdfNames", pdfNames);
		return "pdf-list";
	}

	@GetMapping("/folders")
	public String displayPDF(Model model) throws IOException {

		List<ExerciseSubmission> exerciseSubmission = exerciseSubmission_Repo.findAll();
		Map<ExerciseSubmission, String> submissionsWithEmails = new HashMap<>();
		if(exerciseSubmission != null)
		{
			for(ExerciseSubmission exSubmission : exerciseSubmission)
			{
				String emailAddress = userRepo.findEmailAddressByUsername(exSubmission.getUsername());
				submissionsWithEmails.put(exSubmission, emailAddress);
			}
		}
		model.addAttribute("submissionsWithEmails", submissionsWithEmails);
		return "pdf-list";
	}

	@GetMapping("/basiclab")
	public String assignmentbasic(Model model) throws IOException {
		List<BasicLabSubmission> basicsubmission = basiclab_Repo.findAll();
		model.addAttribute("basicsubmission", basicsubmission);
		// System.out.println(basicsubmission);
		return "AssignBasic";
	}

	@PostMapping("/pdf")
	public ResponseEntity<byte[]> getPdfAdvance(@RequestParam Long id,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            logger.info("HTTP Parameter pollution");
	            // Render the error page and include it in the response
	            return handleBadRequest();
	        }
		}
		ExerciseSubmission submission = exerciseSubmission_Repo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("ExerciseSubmission with id " + id + " not found"));

		// System.out.println(submission);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.builder("inline").filename(submission.getPdfname()).build());

		return new ResponseEntity<>(submission.getSubmission_pdf(), headers, HttpStatus.OK);
	}	
	
	@PostMapping("/pdfbasic")
	public ResponseEntity<byte[]> getPdfBasic(@RequestParam Long id,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            logger.info("HTTP Parameter pollution");
	            // Render the error page and include it in the response
	            return handleBadRequest();
	        }
		}
		BasicLabSubmission submission = basicpdfRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("ExerciseSubmission with id " + id + " not found"));

		// System.out.println(submission);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.builder("inline").filename(submission.getPdfname()).build());

		return new ResponseEntity<>(submission.getSubmission_pdf(), headers, HttpStatus.OK);
	}
	
	private ResponseEntity<byte[]> handleBadRequest() {
	    // Redirect to the /error mapping for bad requests
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(URI.create("/error"));
	    return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
	}
	

	@GetMapping("/CenterDetails")
	public String centerDetails(Model model) {
		List<CenterDetails> centerDetails = addcenterRepo.findAll();
		model.addAttribute("centerDetails", centerDetails);
		return "CenterDetails";

	}

	@GetMapping("/AddCenter")
	public String addCenter() {
		return "AddCenter";

	}

	@GetMapping("/updatecenter/{id}")
	public String update(@PathVariable(value = "id") int id, Model model) {
		List<String> states = Arrays.asList("Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh",
				"Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi",
				"Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka",
				"Kerala", "Ladakh", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
				"Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
				"Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal");
		model.addAttribute("states", states);
		Optional<CenterDetails> centerDtls = addcenterRepo.findById(id);
		model.addAttribute("centerDetails", centerDtls.get()); // Add centerDetails to the model
		return "updateCenter";
	}

	
	
	
	@PostMapping("/updateCenter")
	public String updateUser(HttpSession session,@ModelAttribute @Valid CenterDetails cn,@RequestParam("_csrf") String csrfToken, HttpServletRequest request) throws IOException {

		System.out.println("token is "+csrfToken);
		
		
			CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
	
		
	    if (csrf.getToken().equals(csrfToken)) {
	        // The CSRF token does not match; handle the error as needed, e.g., return an error page.
	    	
	    	cn.getCollege().replaceAll("\\<.*?\\>", "");
	    	cn.getAddress().replaceAll("\\<.*?\\>", "");
	    	cn.getEmailAddress().replaceAll("\\<.*?\\>", "");
	    	cn.getState().replaceAll("\\<.*?\\>", "");
	    	cn.getCity().replaceAll("\\<.*?\\>", "");
	    	cn.getContactMobileNumber().replaceAll("\\<.*?\\>", "");
	    	cn.getContactName().replaceAll("\\<.*?\\>", "");
	    	cn.getContactNumber().replaceAll("\\<.*?\\>", "");
	    	cn.getWebsite().replaceAll("\\<.*?\\>", "");
	    	
	    	
	    	
	    	
	    	String[] params = new String[] { cn.getCollege(), cn.getAddress(),cn.getEmailAddress(),cn.getState()
	    			,cn.getCity(),cn.getContactMobileNumber(),cn.getContactName(),cn.getContactNumber(),cn.getWebsite()};
			String hppCode = cn.getHppCode();
			
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			}
		
		
		CenterDetails center = addcenterRepo.findById(cn.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + cn.getId()));

		// Update the fields that are allowed to be updated
		center.setCollege(cn.getCollege());
		center.setAddress(cn.getAddress());
		center.setCity(cn.getCity());
		center.setState(cn.getState());
		center.setPincode(cn.getPincode());
		center.setContactNumber(cn.getContactNumber());
		center.setEmailAddress(cn.getEmailAddress());
		center.setContactName(cn.getContactName());
		center.setContactMobileNumber(cn.getContactMobileNumber());
		center.setWebsite(cn.getWebsite());
		
			addcenterRepo.save(center);
			session.setAttribute("update", "Center Details Updated Successfully !!!");
			return "redirect:/CenterDetails";


		
	    }
	    return "error.html";
	}
	
	
	@GetMapping("/AddCourse")
	public String addCourse(Model model) {

		List<AddCourse> courseDetails = addcourseRepo.findAll();
		model.addAttribute("courseDetails", courseDetails);
		return "AddCourse";

	}

	@PostMapping("/addcourses")
	public String addCourse(@ModelAttribute @Valid AddCourse add, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request) {

		//System.out.println("token is " + csrfToken);
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		
		add.setCourse(add.getCourse().replaceAll("\\<.*?\\>", ""));
		
		String[] params = new String[] {add.getCourse() };
		String hppCode = add.getHppCode();
		
		if (csrf.getToken().equals(csrfToken)) {
			
			
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			} else {
				addcourseRepo.save(add);
				return "redirect:/AddCourse";
			}
		}
		return "error.html";
	}

	@PostMapping("/addcenter")
	public String addcenter1(HttpSession session, @ModelAttribute @Valid CenterDetails ad,
			@RequestParam("_csrf") String csrfToken, HttpServletRequest request) {
		
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		
		ad.setCollege(ad.getCollege().replaceAll("\\<.*?\\>", ""));
		ad.setAddress(ad.getAddress().replaceAll("\\<.*?\\>", ""));
		ad.setState(ad.getState().replaceAll("\\<.*?\\>", ""));
		ad.setEmailAddress(ad.getEmailAddress().replaceAll("\\<.*?\\>", ""));
		ad.setCity(ad.getCity().replaceAll("\\<.*?\\>", ""));
		ad.setContactMobileNumber(ad.getContactMobileNumber().replaceAll("\\<.*?\\>", ""));
		ad.setContactName(ad.getContactName().replaceAll("\\<.*?\\>", ""));
		ad.setContactNumber(ad.getContactNumber().replaceAll("\\<.*?\\>", ""));
		ad.setWebsite(ad.getWebsite().replaceAll("\\<.*?\\>", ""));
		
		
		String[] params = new String[] { ad.getCollege(), ad.getAddress(),ad.getState(),ad.getEmailAddress()
				,ad.getCity(),ad.getContactMobileNumber(),ad.getContactName(),ad.getContactNumber(),ad.getWebsite(),ad.getPincode()};
		String hppCode = ad.getHppCode();
		
		
		if (csrf.getToken().equals(csrfToken)) {
		
			
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			} else {
				addcenterRepo.save(ad);
				session.setAttribute("add", "Center Added Successfully !!!");
				return "AddCenter";
			}

		

		}
		return "error.html";
	}

	@PostMapping("/savecenter")
	public String savecenter(@ModelAttribute @Valid CenterDetails ab, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request) {
		System.out.println("token is " + csrfToken);
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		
		ab.setAddress(ab.getAddress().replaceAll("\\<.*?\\>", ""));
		ab.setCollege(ab.getCollege().replaceAll("\\<.*?\\>", ""));
		
		String[] params = new String[] {ab.getAddress(),ab.getCollege() };
		String hppCode = ab.getHppCode();
		
		if (csrf.getToken().equals(csrfToken)) {
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			} else {
				addcenterRepo.save(ab);
				return "redirect:/CenterDetails";
			}
		}
		return "error.html";
	}

	@PostMapping("/deletecenter/{id}")
	public String Deletecenter(@PathVariable(value = "id") int id,HttpSession session,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode ) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}
		addcenterRepo.deleteById(id);
		return "redirect:/CenterDetails";

	}

	@PostMapping("/deletecourse/{id}")
	public String deletecourse(@PathVariable(name = "id") int id ,HttpSession session,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode ) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(id);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}

		addcourseRepo.deleteById(id);
		return "redirect:/AddCourse";

	}

	/// for lab details
	/// for lab details
	@GetMapping("/LabDetails")
	public ModelAndView labDetails(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registrationDate) {
		ModelAndView modelAndView = new ModelAndView();
		List<StudentDtls> approvedStudents;

		if (registrationDate != null) {

			// approvedStudents =
			// studentRepo.findByApprovedAndRegistrationDateGreaterThanEqual(true,
			// registrationDate);
			approvedStudents = studentRepo.findByApproveAndRole(true, "USER");

		} else {
//			approvedStudents = studentRepo.findByApproved(true);
//			System.out.println("users are "+studentRepo.findByApproveAndRole(true,"USER"));
			approvedStudents = studentRepo.findByApproveAndRole(true, "USER");
		}

		List<String> passwords = new ArrayList<>();
		List<String> username = new ArrayList<>();

		for (StudentDtls student : approvedStudents) {

			String username1 = student.getEmailAddress();

			String username2 = username1.substring(0, username1.indexOf('@')) + '@' + "cybergyan.in";

			username.add(username2);

			String uname = student.getFirstName();
			
			if(uname.length()>=3)
			{
			uname = uname.substring(0, 3);
			String password = uname + "@@"
					+ String.format("%02d%02d", student.getDob().getDayOfMonth(), student.getDob().getMonthValue());
			passwords.add(password);
			}else if(uname.length()==2)
			{
				uname = uname.substring(0, 2);
				String password = uname + "@@@"
						+ String.format("%02d%02d", student.getDob().getDayOfMonth(), student.getDob().getMonthValue());
				passwords.add(password);
					
			}else 
			{
				uname = uname.substring(0, 1);
				String password = uname + "@@@@"
						+ String.format("%02d%02d", student.getDob().getDayOfMonth(), student.getDob().getMonthValue());
				passwords.add(password);
					
			}
		}

		modelAndView.addObject("username", username);
		modelAndView.addObject("approvedStudents", approvedStudents);
		modelAndView.addObject("passwords", passwords);
		modelAndView.addObject("registrationDate", registrationDate);
		modelAndView.setViewName("LabDetails.html");
		return modelAndView;
	}

	@Autowired
	private VideoRepository videoRepo;

	@GetMapping("/video")
	public ModelAndView videoupload() {
		ModelAndView modelAndView = new ModelAndView();
		List<Video> videos = videoRepo.findAll();
		modelAndView.addObject("videos", videos);
		modelAndView.setViewName("Videoupload.html");
		return modelAndView;
	}

	@GetMapping("/LabCompletion")
	public String LabCompletionStatus(Model model) {
		List<StudentDtls> studentDtls = studentRepo.findByApproveAndRole(true, "USER");

		Map<String, Double> percent = new HashMap<>();

		for (StudentDtls stu : studentDtls) {

			String str = stu.getEmailAddress().substring(0, stu.getEmailAddress().indexOf('@')) + '@' + "cybergyan.in";

			Integer exsb = exerciseSubmission_Repo.findByEmail(str);

			if (exsb != null && exsb > 0) { // Check if exsb is not null and greater than zero
				if (stu.getBatch() > 0) {
					Integer batch = batch_Master_repo.findDtlsByBatch_no(stu.getBatch());
					if (batch != null) {
						// System.out.println(batch);
						double per = ((double) exsb / batch) * 100;
						percent.put(stu.getEmailAddress(), per);
					}
				} else {
					Integer classExercise = classExerciseRepo.getByClassName(stu.getClassName());
					if (classExercise != null) {
						/* System.out.println(classExercise); */
						double per = ((double) exsb / classExercise) * 100;
						percent.put(stu.getEmailAddress(), per);
					}
				}
			} else {
				// Handle the case where exsb is null or zero (to avoid division by zero)
				percent.put(stu.getEmailAddress(), 0.0); // You can set the percentage to 0 or handle it differently
			}
		}

		model.addAttribute("studentDtls", studentDtls);
		model.addAttribute("percent", percent);

		return "LabCompletion";
	}
	
	@GetMapping("/quizCompletion")
	public String QuizCompletionStatus(Model model) {
	    // Fetch all quiz attempts where quiz is published
	    List<QuizUserAttempt> quizUserAttempt = quizUserAttemptRepository.findAllPublished();

	    // Create a map to store quiz attempts by student
	    Map<String, List<Integer>> quizlist = new TreeMap<>();

	    // Fetch all approved students
	    List<StudentDtls> studentId = studentRepo.findByApprovedAndRole(true, "USER");

	    // Add approved students to the model
	    model.addAttribute("studentId", studentId);

	    // Iterate over each student
	    for (StudentDtls a : studentId) {
	        // Fetch quizzes attempted by the student where the quiz is published
	        List<Integer> quizesofuser = quizUserAttemptRepository.findByUserId(a.getId(), true);
	        // Add quiz attempts to the map
	        quizlist.put(a.getFirstName() + " " + a.getLastName() + " (" + a.getEmailAddress() + ") (" + a.getId() + ")", quizesofuser);
	    }

	    // Print quiz attempts for debugging
	    for (Map.Entry<String, List<Integer>> entry : quizlist.entrySet()) {
	        String key = entry.getKey();
	        List<Integer> value = entry.getValue();
	        System.out.print("Key: " + key + ", Value: ");
	        for (Integer item : value) {
	            System.out.print(item + " ");
	        }
	        System.out.println();
	    }

	    // Add quiz attempts by student to the model
	    model.addAttribute("quizlist", quizlist);

	    // Fetch unique quiz IDs for dropdown
	    SortedSet<String> uniqueQuizIds = new TreeSet<>();
	    for (QuizUserAttempt q : quizUserAttempt) {
	        Quiz quiz = quizRepo.findById(q.getQuizId()).get();
	        uniqueQuizIds.add(quiz.getId() + " " + quiz.getQuizTitle());
	    }

	    // Add unique quiz IDs to the model
	    model.addAttribute("uniqueQuizIds", uniqueQuizIds);

	    // Add other attributes as needed
	    model.addAttribute("quizUserAttempt", quizUserAttempt);

	    // Return the view name
	    return "QuizCompletionStatus";
	}

	
	@GetMapping("/batchCompletion")
    public String batchCompletionStatus(@RequestParam(value = "batch", required = false) String batchNumber, Model model) {

		List<Integer> distinctBatches = studentRepo.findDistinctBatches();
        model.addAttribute("distinctBatches", distinctBatches);
              	
        return "BatchCompletionStatus";
    }
	
	@GetMapping("/getStudentsByBatch")
	@ResponseBody
	public String getStudentsByBatch(@RequestParam("batch") int batchNum) {
		List<BatchCompletionStatus> list = new ArrayList<>();
		List<StudentDtls> students = studentRepo.findByBatch(batchNum);
		for (StudentDtls s : students) {
			BatchCompletionStatus batchCompletionStatus = new BatchCompletionStatus();
			batchCompletionStatus.setStuId(s.getId());
			batchCompletionStatus.setFirstName(s.getFirstName());
			batchCompletionStatus.setLastName(s.getLastName());
			batchCompletionStatus.setEmail(s.getEmailAddress());
			batchCompletionStatus.setMobileNumber(s.getMobileNumber());
			StudentVideoWatchInf studentVideoWatchInf = studentVideoWatchInfRepository
					.findByUserIdAndCourseNameContaining(s.getId(), "Basic");
			if (studentVideoWatchInf != null)
				batchCompletionStatus.setBasicLevelStatus((float) (studentVideoWatchInf.getPercetageStatus()));
			else
				batchCompletionStatus.setBasicLevelStatus(0);
			String str = s.getEmailAddress().substring(0, s.getEmailAddress().indexOf('@')) + '@' + "cybergyan.in";

			Integer exsb = exerciseSubmission_Repo.findByEmail(str);

			if (exsb != null && exsb > 0) { // Check if exsb is not null and greater than zero
				if (s.getBatch() > 0) {
					Integer batch = batch_Master_repo.findDtlsByBatch_no(s.getBatch());
					if (batch != null) {
						// System.out.println(batch);
						double per = ((double) exsb / batch) * 100;
						batchCompletionStatus.seteLabStatus((float) per);
					}
				} else {
					Integer classExercise = classExerciseRepo.getByClassName(s.getClassName());
					if (classExercise != null) {
						/* System.out.println(classExercise); */
						double per = ((double) exsb / classExercise) * 100;
						batchCompletionStatus.seteLabStatus((float) per);
					}
				}
			} else {
				// Handle the case where exsb is null or zero (to avoid division by zero)
				batchCompletionStatus.seteLabStatus(0.0f);
			}
			float quizStatus = ((float) quizUserAttemptRepository.countByStudentAndPublishedIsTrue(s)
					/ quizRepo.countPublishedQuizzes()) * 100;
			batchCompletionStatus.setQuizStatus(quizStatus);
			list.add(batchCompletionStatus);
		}

		StringBuilder html = new StringBuilder();
		int serialNumber = 1;
		for (BatchCompletionStatus student : list) {
			html.append("<tr>");
			html.append("<td class=\"text-center\">").append(serialNumber).append("</td>");
			// Append other student details
			html.append("<td class=\"text-center\">").append(student.getStuId()).append("</td>");
			html.append("<td class=\"text-center\">").append(student.getFirstName()).append("</td>");
			html.append("<td class=\"text-center\">").append(student.getLastName()).append("</td>");
			html.append("<td class=\"text-center\">").append(student.getEmail()).append("</td>");
			html.append("<td class=\"text-center\">").append(student.getMobileNumber()).append("</td>");
			html.append("<td class=\"text-center\">").append(String.format("%.2f", student.getBasicLevelStatus()))
					.append(" %</td>");
			html.append("<td class=\"text-center\">").append(String.format("%.2f", student.geteLabStatus()))
					.append(" %</td>");
			html.append("<td class=\"text-center\">").append(String.format("%.2f", student.getQuizStatus()))
					.append(" %</td>");
			html.append("</tr>");
			serialNumber++;
		}
		return html.toString();
	}
	
	@GetMapping("/AddExerciseContent")
	public String addExerciseContent(Model model,@RequestParam(name = "msg", required = false) String message,@RequestParam(name = "msg1", required = false) String message1)
	{
		List<Exercise_Master> courseTopics = new ArrayList<>();
		List<Long> exerciseIds = exerciseContentRepository.findAllExerciseIds();
		for(Exercise_Master exercise_Master: exercise_MasterRepo.findAll()) {
			if(exerciseIds.contains(exercise_Master.getEx_id())) {
				continue;
			}
			courseTopics.add(exercise_Master);
		}
		model.addAttribute("courseTopics", courseTopics);
		model.addAttribute("msg", message);
		model.addAttribute("msg1", message1);
		return "AddExerciseContent";
	}
	@PostMapping("/saveExerciseContent")
	public String saveExerciseContent(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	    
		
		// Retrieve values of hidden fields
	    String exerciseId = request.getParameter("exerciseId");
	    String exerciseHeadingName = request.getParameter("exerciseHeadingName");
	    String title = request.getParameter("title");
	    String learningObjective = request.getParameter("learningObjective");
	    String description = request.getParameter("description");
	    String labInfraStructure = request.getParameter("labInfraStructure");
	    String attackProcedure = request.getParameter("attackProcedure");
	    String hppCode = request.getParameter("hppCode");
	    
		String[] params = new String[] {exerciseId, exerciseHeadingName, title, learningObjective, description, labInfraStructure, attackProcedure};
		if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
			logger.info("HTTP Parameter pollution");
			return "error.html";
		}
	    
	    ExerciseContent exerciseContent = exerciseContentRepository.findByExerciseId(Integer.parseInt(exerciseId));
	    
	    if(exerciseContent != null)
	    {
	    	redirectAttributes.addAttribute("msg1", "Exercise Content All Ready Exists..");
		    return "redirect:/AddExerciseContent";
	    }
	    
	    ExerciseContent exContent = new ExerciseContent();
	    exContent.setExerciseId(Integer.parseInt(exerciseId));
	    exContent.setExerciseHeadingName(exerciseHeadingName);
	    exContent.setTitle(title);
	    exContent.setLearningObjective(learningObjective);
	    exContent.setDescription(description);
	    exContent.setLabInfraStructure(labInfraStructure);
	    exContent.setAttackProcedure(attackProcedure);
	    exerciseContentRepository.save(exContent);
	    redirectAttributes.addAttribute("msg", "Exercise Content Added Successfully..");
	    return "redirect:/AddExerciseContent";
	}
	
	@GetMapping("/updateExerciseContent")
	public String updateExerciseContent(Model model,@ModelAttribute("msg") String message,@ModelAttribute("msg1") String message1)
	{
		List<Exercise_Master> courseTopics = new ArrayList<>();
		List<Long> exerciseIds = exerciseContentRepository.findAllExerciseIds();
		for(Exercise_Master exercise_Master: exercise_MasterRepo.findAll()) {
			if(exerciseIds.contains(exercise_Master.getEx_id())) {
				courseTopics.add(exercise_Master);
			}
		}
		model.addAttribute("courseTopics", courseTopics);
		model.addAttribute("msg", message);
		model.addAttribute("msg1", message1);
		return "UpdateExerciseContent";
	}
	
	@GetMapping("/exerciseContent")
	@ResponseBody
	public ExerciseContent getExerciseContent(@RequestParam("exerciseId") String exerciseId) {
		ExerciseContent exerciseContent = exerciseContentRepository.findByExerciseId(Integer.parseInt(exerciseId));
		if(exerciseContent == null)
			return new ExerciseContent();
		return exerciseContent;
	}
	
	@PostMapping("/updateExerciseContent")
	public String updateExerciseContent(HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
	    // Retrieve values of hidden fields
	    String exerciseId = request.getParameter("exerciseId");
	    String exerciseHeadingName = request.getParameter("exerciseHeadingName");
	    String title = request.getParameter("title");
	    String learningObjective = request.getParameter("learningObjective");
	    String description = request.getParameter("description");
	    String labInfraStructure = request.getParameter("labInfraStructure");
	    String attackProcedure = request.getParameter("attackProcedure");
	    String hppCode = request.getParameter("hppCode");
	    
	    String[] params = new String[] {exerciseId, exerciseHeadingName, title, learningObjective, description, labInfraStructure, attackProcedure};
		if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
			logger.info("HTTP Parameter pollution");
			return "error.html";
		}
	    
	    ExerciseContent exerciseContent = exerciseContentRepository.findByExerciseId(Integer.parseInt(exerciseId));
	    
	    if(exerciseContent != null)
	    {
	    	exerciseContent.setExerciseHeadingName(exerciseHeadingName);
	 	    exerciseContent.setTitle(title);
	 	    exerciseContent.setLearningObjective(learningObjective);
	 	    exerciseContent.setDescription(description);
	 	    exerciseContent.setLabInfraStructure(labInfraStructure);
	 	    exerciseContent.setAttackProcedure(attackProcedure);
	 	    exerciseContentRepository.save(exerciseContent);
	 	    redirectAttributes.addAttribute("msg", "Exercise Content Update Successfully..");
		    return "redirect:/updateExerciseContent";
	    }
	    
	    ExerciseContent exContent = new ExerciseContent();
	    exContent.setExerciseId(Integer.parseInt(exerciseId));
	    exContent.setExerciseHeadingName(exerciseHeadingName);
	    exContent.setTitle(title);
	    exContent.setLearningObjective(learningObjective);
	    exContent.setDescription(description);
	    exContent.setLabInfraStructure(labInfraStructure);
	    exContent.setAttackProcedure(attackProcedure);
	    exerciseContentRepository.save(exContent);
	    redirectAttributes.addAttribute("msg", "Exercise Content Added Successfully..");
	    return "redirect:/updateExerciseContent";
	}
	
	@GetMapping("/AddExerciseImage")
	public String AddExerciseImage(Model model, @ModelAttribute("msg") String message)
	{
		List<Exercise_Master> exerciseMasters = exercise_MasterRepo.findAll();
		model.addAttribute("exerciseMasters", exerciseMasters);
		model.addAttribute("msg", message);
		return "AddExerciseImage";
	}
	
	@Value("${upload.directory}")
    private String uploadDirectory;
	
	@PostMapping("/SaveExerciseImage")
    public String SaveExerciseImage(@RequestParam("courseTopicId") int courseTopicId,
                                    @RequestParam("ArchitectureImage") MultipartFile architectureImage,
                                    @RequestParam("GUIImage") MultipartFile guiImage,@RequestParam(value="hppCode") String hppCode,
                                    RedirectAttributes redirectAttributes) throws IOException {
		
		String[] params = new String[] {String.valueOf(courseTopicId), architectureImage.getOriginalFilename(), guiImage.getOriginalFilename()};
		if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
			logger.info("HTTP Parameter pollution");
			return "error.html";
		}

		
		exerciseImageService.saveImage(uploadDirectory, courseTopicId, architectureImage, guiImage);
        redirectAttributes.addFlashAttribute("msg", "Exercise Images Added Successfully..");
        return "redirect:/AddExerciseImage";
    }

	@GetMapping("/StudentTrackTime")
	public String studentTrackTime(Model model) {
	    List<StudentTrackTime> studentTrackTimes = studentTrackTimeRepo.findAll();
	    Map<String, Long> durationMap = new HashMap<>();
	    
	    for (StudentTrackTime STT : studentTrackTimes) {
	        if(STT.getTimeSpentInPortal() != null)
	        {
	        	long minutes = STT.getTimeSpentInPortal().toMinutes();
		        durationMap.put(STT.getUsername(), minutes);
		        //System.out.println("Duration for " + STT.getUsername() + " : " + minutes + " minutes");
	        }
	    }
	    
	    model.addAttribute("duration", durationMap);
	    model.addAttribute("studentTrackTimes", studentTrackTimes);
	    return "StudentTrackTime";
	}

	@GetMapping("/deleteAllVmDetails")
	public String deleteAllVmDetails(Model model, @RequestParam(value = "msg", required = false) String msg)
	{
		List<AdvanceLabUserVmDetails> advanceLabUserVmDetails = advanceLabUserVmDetailsRepository.findAll();
		model.addAttribute("advanceLabUserVmDetails", advanceLabUserVmDetails);
		if (msg != null && !msg.isEmpty()) {
	        model.addAttribute("msg", msg);
	    }
		return "deleteAllVmDetails";
	}
	
	@PostMapping("/deleteVms")
	@Transactional
	public String deleteVms(@RequestParam(value ="selectedVms" , required = false) List<String> selectedVms, RedirectAttributes redirectAttributes) {
		
		if (selectedVms == null || selectedVms.isEmpty()) {
	        redirectAttributes.addFlashAttribute("msg", "No VMs selected for deletion.");
	        return "redirect:/deleteAllVmDetails";
	    }
		
		List<AdvanceLabUserVmDetails> advanceLabUserVmDetails = new ArrayList<>();
		for (String idStr : selectedVms) {
            Long id = Long.parseLong(idStr);
            advanceLabUserVmDetailsRepository.findById(id).ifPresent(advanceLabUserVmDetails::add);
        }
	    try {
	       for(AdvanceLabUserVmDetails ALUVD : advanceLabUserVmDetails)
	       {
	    	   advanceLabService.deleteVm(ALUVD.getUUID());
	    	   TimeUnit.SECONDS.sleep(10);
			   String authToken = guacamoleService.getAdminToken().get();
			   String connectionIdentifier = guacamoleService.getConnectionIdentifier(ALUVD.getVmName(), authToken).get();
			   boolean status = guacamoleService.deleteConnection(connectionIdentifier, authToken).get();
			   advanceLabUserVmDetailsRepository.deleteById(ALUVD.getId());
			   if(!status)
			   {
				   redirectAttributes.addFlashAttribute("msg", "An error occurred while deleting connection " + ALUVD.getVmName() + " from Guacamole kindly delete manually.");
				   return "redirect:/deleteAllVmDetails";
			   }
	       }
	       redirectAttributes.addFlashAttribute("msg", "Successfully deleted.");
	       return "redirect:/deleteAllVmDetails";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("msg", "An error occurred while deleting.");
		    return "redirect:/deleteAllVmDetails";
	    }
	}
}