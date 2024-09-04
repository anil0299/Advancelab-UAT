package com.Advanceelab.cdacelabAdvance.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Advanceelab.cdacelabAdvance.entity.BasicLabSubmission;
import com.Advanceelab.cdacelabAdvance.entity.ClassExercise;
import com.Advanceelab.cdacelabAdvance.entity.CreateClass;
import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;
import com.Advanceelab.cdacelabAdvance.entity.RejectedStudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.StudentVideoWatchInf;
import com.Advanceelab.cdacelabAdvance.entity.TeacherDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.mailSender.EmailSenderService;
import com.Advanceelab.cdacelabAdvance.repository.AddCenterRepo;
import com.Advanceelab.cdacelabAdvance.repository.AddCoursesRepo;
import com.Advanceelab.cdacelabAdvance.repository.ClassExerciseRepository;
import com.Advanceelab.cdacelabAdvance.repository.CreateClassRepo;
import com.Advanceelab.cdacelabAdvance.repository.ExerciseSubmission_Repo;
import com.Advanceelab.cdacelabAdvance.repository.Exercise_MasterRepo;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentVideoWatchInfRepository;
import com.Advanceelab.cdacelabAdvance.repository.TeacherRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.security.EncodingUtility2;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;
import com.Advanceelab.cdacelabAdvance.service.ReturnObject;

@Controller
public class TeacherController {

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private AddCoursesRepo addcoursesRepo;

	@Autowired
	private AddCenterRepo addcenterRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TeacherRepository teacherRepo;

	@Autowired
	private Exercise_MasterRepo exer_repo;

	@Autowired
	private CreateClassRepo createclassRepo;

	@Autowired
	private ClassExerciseRepository classExerRepo;

	@Autowired
	private StudentVideoWatchInfRepository studentVideoInfo;

	@Autowired
	private ExerciseSubmission_Repo exerciseSubmission_Repo;

	@Autowired
	private EmailSenderService senderService;

	private Logger logger = Logger.getLogger(getClass().getName());

	/*
	 * @GetMapping("/TeacherRegistration") public ModelAndView
	 * adminhome(ModelAndView modelandview) {
	 * modelandview.setViewName("TeacherRegistration.html");
	 * 
	 * return modelandview;
	 * 
	 * }
	 */

	@GetMapping("/TeacherRegistration")
	public String register(Model model) {

		List<String> courses = addcoursesRepo.findCourse();
		List<String> states = addcenterRepo.findDistinctState();
		model.addAttribute("courses", courses);
		model.addAttribute("states", states);
//		getColleges("Uttar Pradesh");

		return "TeacherRegistration";

	}
	
	@GetMapping("/checkMobileNumber2")
	@ResponseBody
	public String checkMobileNumber2(@RequestParam("mobileNumber") String mobile) {
		boolean teacherExists = teacherRepo.existsByMobileNumber(mobile);
		boolean studentExists = studentRepo.existsByMobileNumber(mobile);
		if (teacherExists || studentExists) {
			//System.out.println("exists");
			return "exists";
		} else {
			//System.out.println("not exists");
			return "notExists";
		}
		
	}

	@GetMapping("/checkUniqueEmailteacher")
	@ResponseBody
	public String checkUniqueEmailteacher(@RequestParam("email") String email) {
		boolean exists = teacherRepo.existsByEmailAddress(email);

		if (exists) {
			// System.out.println("exists");
			return "exists";
		} else {
			// System.out.println("not exists");
			return "notExists";
		}
	}

	@PostMapping("/registerTeacher")
	public String createuser(HttpSession session, @RequestParam("state") String state,
			@RequestParam("college") String college, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("fatherName") String fatherName,
			@RequestParam("gender") String gender, @RequestParam(value = "dob", required = false) String dobStr,
			@RequestParam("mobileNumber") String mobileNumber, @RequestParam("password") String password,
			@RequestParam("IdProof") MultipartFile file, @RequestParam("emailAddress") String emailAddress,
			@RequestParam(value = "hppCode") String hppCode, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request, Model model) throws IOException {
		
		emailAddress = emailAddress.toLowerCase();
		
		byte[] pdffile = file.getBytes();

		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			// The CSRF token does not match; handle the error as needed, e.g., return an
			// error page.

			try (PDDocument pdfDocument = PDDocument.load(file.getInputStream())) {
				PDFTextStripper textStripper = new PDFTextStripper();
				String pdfContent = textStripper.getText(pdfDocument);

				if (pdfContent.contains("<script>")) {
					session.setAttribute("msg1", "Your Government is malicious.");
					return "redirect:/TeacherRegistration";
				}
			} catch (Exception e) {
				// Handle exceptions (e.g., PDF processing errors)
				e.printStackTrace();
				// Add an error message to the model if needed
				return "redirect:/TeacherRegistration";
			}

			// create an instance of BCryptPasswordEncoder with a strength of 10
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

			try {
				StudentDtls existingStudent = studentRepo.findByEmailAddress(emailAddress);
				if (existingStudent != null) {
					session.setAttribute("sameEmailMsg",
							"A user with the Email  " + emailAddress + " already exists.");
					return "redirect:/TeacherRegistration";
				}
			} catch (NonUniqueResultException ex) {
				session.setAttribute("sameEmailMsg",
						"Multiple user with the Email " + emailAddress + " found in the database.");
				return "redirect:/TeacherRegistration";
			}

			TeacherDtls teacher = new TeacherDtls();
			teacher.setState(state.replaceAll("\\<.*?\\>", ""));
			teacher.setCollege(college.replaceAll("\\<.*?\\>", ""));
			teacher.setGender(gender);
			teacher.setFirstName(firstName.replaceAll("\\<.*?\\>", ""));
			teacher.setLastName(lastName.replaceAll("\\<.*?\\>", ""));
			teacher.setFatherName(fatherName.replaceAll("\\<.*?\\>", ""));
			 int seed=5;
	        password=EncodingUtility2.decodeWithSeedNEW(password, seed);
			String[] params = new String[] { state, college,firstName,lastName,fatherName,mobileNumber,emailAddress,gender,dobStr,password };
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
				return "error.html";
			}
			// validate and parse the date
			LocalDate dob = null;
			try {
				dob = LocalDate.parse(dobStr);
			} catch (DateTimeParseException ex) {
				// handle invalid date
				throw new IllegalArgumentException("Invalid date format: " + dobStr);
			}

			teacher.setDob(dob);
			teacher.setEmailAddress(emailAddress.replaceAll("\\<.*?\\>", ""));
			teacher.setMobileNumber(mobileNumber.replaceAll("\\<.*?\\>", ""));
			teacher.setPassword(encoder.encode(password));
			teacher.setIdProof(pdffile);
			teacher.setRegistrationDate(LocalDate.now());

			

			teacherRepo.save(teacher);
			session.setAttribute("msg", "Registered Successfully !!!");
			session.setAttribute("msg1", "You will get an approval e-mail from the administrator.");

			return "redirect:/TeacherRegistration";
		} else {
			return "error.html";
		}
	}

	@GetMapping("/TeacherApproval")
	public String getStudentDtls(Model model) {
		List<TeacherDtls> teacherDtls = teacherRepo.findByApproved(false);
		model.addAttribute("teacherDtls", teacherDtls);

		List<TeacherDtls> list = teacherRepo.findByApproved(true);
		model.addAttribute("teacherDtls1", list);

		return "TeacherApproval";
	}

	@RequestMapping("/TeacherApproval/approve-teacherDtls/{teacherDtlsId}")
	public String approveTeacherDtls(@PathVariable(value = "teacherDtlsId") int id, Model model,HttpSession session,@RequestParam(value = "hppCode") String hppCode) {
		Optional<TeacherDtls> teacherDtlsOptional = teacherRepo.findById(id);

		String a=String.valueOf(id);
String[] params = new String[] { a };
		
		if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
			logger.info("HTTP Parameter pollution");

			return "error.html";
		}
		
		if (teacherDtlsOptional.isPresent()) {
			TeacherDtls teacher = teacherDtlsOptional.get();
			teacher.setApproved(true);

			/*
			 * teacher.setState(teacher.getState().replaceAll("\\<.*?\\>", ""));
			 * teacher.setCollege(teacher.getCollege().replaceAll("\\<.*?\\>", ""));
			 */

			//System.out.println(hppCode);

			/*
			 * String[] params = new String[] { state, college };
			 * 
			 * if (!RequestParameterValidationUtility.validateRequestForHPP(params,
			 * hppCode)) { logger.info("HTTP Parameter pollution"); return "error.html"; }
			 */

			teacherRepo.save(teacher);
					
			
			if(studentRepo.existsByEmailAddress(teacher.getEmailAddress())) {
				return "redirect:/TeacherApproval";
			}
			
			
			// Create a new StudentDtls object to represent the approved teacher
			StudentDtls approvedTeacher = new StudentDtls();
			approvedTeacher.setState(teacher.getState());
			approvedTeacher.setCollege(teacher.getCollege());
			approvedTeacher.setGender(teacher.getGender());
			approvedTeacher.setFirstName(teacher.getFirstName());
			approvedTeacher.setLastName(teacher.getLastName());
			approvedTeacher.setFatherName(teacher.getFatherName());
			approvedTeacher.setDob(teacher.getDob());
			approvedTeacher.setEmailAddress(teacher.getEmailAddress());
			approvedTeacher.setMobileNumber(teacher.getMobileNumber());
			approvedTeacher.setPassword(teacher.getPassword());
			approvedTeacher.setIdProof(teacher.getIdProof());
			approvedTeacher.setRegistrationDate(teacher.getRegistrationDate());
			// Copy any other relevant details from the teacher object to the
			// approvedTeacher object

			// Mark the approvedTeacher as a teacher by setting the role to "TEACHER"
			approvedTeacher.setRole("TEACHER");

			// Mark the approvedTeacher as approved
			approvedTeacher.setApproved(true);

			// Save the approvedTeacher object in the StudentRepo
			studentRepo.save(approvedTeacher);
			
			
			if(userRepo.existsByUsernameOrEmailAddress(teacher.getEmailAddress(), teacher.getEmailAddress()))
			{
				return "redirect:/TeacherApproval";
			}
			User user = new User();
			user.setBatch(0);
			user.setRole("TEACHER");
			user.setUsername(teacher.getEmailAddress());
			user.setEmailAddress(teacher.getEmailAddress());
			user.setPassword(teacher.getPassword());
			user.setApproved(true);
			user.setLoginAttempt(0);
			userRepo.save(user);

			senderService.sendEmail(teacher.getEmailAddress(), "Cyber Gyan", "Dear Faculty," + "\n"
					+ "Welcome to the CyberGyan portal." + "\n"
					+ "You can now access the course on \"Ethical Hacking and Penetration Testing\" , create class for your students. add your students to the class and monitor their progress."
					+ "\n" + "Access the portal using the following link: https://cdaccybergyan.uat.dcservices.in/welcome" + "\n" + "\n"
					+ "Please get back to us for any querie at cybergyan-noida@cdac.in" + "\n" + "CyberGyan Team.");

		}
		session.setAttribute("msg", "Teacher Approved Successfully !!!");
		return "redirect:/TeacherApproval";
	}
	

	@PostMapping("/displayPDF5/{id}")
	public ModelAndView displayPDF(@PathVariable int id, HttpServletResponse response,
	                               @RequestParam("_csrf") String csrfToken, HttpServletRequest request,
	                               @RequestParam(value = "hppCode") String hppCode) {
	    CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
	    ModelAndView modelAndView = new ModelAndView();

	    if (csrf.getToken().equals(csrfToken)) {
	        String a = String.valueOf(id);
	        String[] params = new String[]{a};

	        if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            logger.info("HTTP Parameter pollution");
	            // Set the view name to "error" and add an errorMessage attribute
	            modelAndView.setViewName("error");
	            modelAndView.addObject("errorMessage", "HTTP Parameter pollution");
	            return modelAndView;
	        }
	    }

	    try {
	        TeacherDtls teacher = teacherRepo.findById(id).orElseThrow();
	        byte[] pdffile = teacher.getIdProof();
	        response.setContentType("application/pdf");
	        response.setHeader("Content-disposition", "inline; filename=file.pdf");
	        response.setContentLength(pdffile.length);
	        response.getOutputStream().write(pdffile);
	        response.getOutputStream().flush();
	    } catch (Exception e) {
	        // handle the case when the student is not found
	        modelAndView.setViewName("error");
	        modelAndView.addObject("errorMessage", "Student not found");
	        modelAndView.setStatus(HttpStatus.NOT_FOUND);
	    }

	    return modelAndView;
	}
	
	
	
	
	@RequestMapping("/deleteTeacher/{id}")
	public String rejectuser(@PathVariable(name = "id") int id, @RequestParam(value = "hppCode") String hppCode) {

		String a=String.valueOf(id);
		String[] params = new String[] { a };
				
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			}
		Optional<TeacherDtls> student = teacherRepo.findById(id);
		List<User> list = userRepo.findAll();

//		  if (student.isPresent()) { 
//			  StudentDtls studentDtls = student.get(); String
//		  studentEmail = studentDtls.getEmailAddress();
//		  
//		  
//		  int atIndex = studentEmail.indexOf('@'); String username =
//		  studentEmail.substring(0, atIndex); String domain = username;
//		  
//		  String newEmail = domain + "@cybergyan.in";
//		  
//		  
//		  for (User user : list) { if (user.getUsername().equals(newEmail)) { // found
//		  a matching user // do something with the user object // stop searching once a
//		  match is found userRepo.delete(user); break; } } }
//		 

		teacherRepo.deleteById(id);
		return "redirect:/TeacherApproval";
	}

	@GetMapping("/TeacherHome")
	public ModelAndView TeacherHome(ModelAndView modelandview) {
		modelandview.setViewName("TeacherHome.html");

		return modelandview;

	}

	@GetMapping("/CompletionStatusTeacherView")
	public String completionStatusTeacherView(Model model, Principal principal) {
		String email = principal.getName();
		List<String> classes = createclassRepo.findClassNameByEmail(email);
		model.addAttribute("classes", classes);

		return "CompletionStatusTeacherView.html";
	}

	@GetMapping("/search/api/getSearchResult/{id}")
	@ResponseBody
	public ReturnObject getSearchResultViaAjax(@PathVariable(value = "id") String id) {

		List<StudentDtls> students = studentRepo.getTeacherStatus(id);

		Integer CE = classExerRepo.getByClassName(id);
		Map<String, String> mp = new HashMap<>();

		for (StudentDtls studentDtls : students) {
			String email = studentDtls.getEmailAddress();
			String email1 = email.substring(0, email.indexOf('@')) + "@cybergyan.in";
			Integer ES = exerciseSubmission_Repo.findByEmail(email1);

			Double per = ((double) ES / CE) * 100;
			mp.put(studentDtls.getEmailAddress(), String.format(("%.2f"), per));
		}

		ReturnObject returnObject = new ReturnObject(students, mp);

		/*
		 * for (StudentDtls stu : students) {
		 * System.out.println("email : "+stu.getEmailAddress()+"percentage = "+mp.get(
		 * stu.getEmailAddress())); }
		 */

		/*
		 * List<HashMap<String, String>> list = new ArrayList<>(); SearchResult
		 * searchResult = new SearchResult(); for (StudentDtls stu : students) {
		 * HashMap<String, String> hashMap = new HashMap<>(); hashMap.put("id",
		 * String.valueOf(stu.getId())); hashMap.put("name", stu.getFirstName());
		 * hashMap.put("category", stu.getCategory()); hashMap.put("email",
		 * stu.getEmailAddress()); list.add(hashMap);
		 * 
		 * 
		 * 
		 * List<Object[]> result = studentRepo.getPercentCount(); List<Map<String,
		 * Object>> formattedResult = new ArrayList<>();
		 * 
		 * for (Object[] row : result) { Map<String, Object> map = new HashMap<>();
		 * map.put("userId", row[0]); Object obj = row[0]; String str = obj.toString();
		 * 
		 * int atIndex = str.indexOf('@'); String username = str.substring(0, atIndex);
		 * String domain = username;
		 * 
		 * String newEmail = domain + "@gmail.com"; map.put("username", newEmail);
		 * 
		 * int submissionCount = ((BigInteger) row[1]).intValue(); int t =
		 * submissionCount * 10;
		 * 
		 * map.put("submissionCount", t); formattedResult.add(map); }
		 * 
		 * searchResult.setList(list); searchResult.setFormattedResult(formattedResult);
		 * 
		 * }
		 * 
		 * // System.out.println("Message from the controller: Processing search result
		 * for // ID " + id);
		 */
		return returnObject;
	}

	// lab Completion

	@GetMapping("/CompletionStatusTeacherlms")
	public String CompletionStatusTeacherlms(Model model, Principal principal) {
		String email = principal.getName();
		List<String> classes = createclassRepo.findClassNameByEmail(email);
		model.addAttribute("classes", classes);

		return "Completionstatus2.html";
	}

	@GetMapping("/getStudentlist")
	@ResponseBody
	public String getStudentListByClass(@RequestParam("className") String className) {
		// System.out.println("ClassName"+className);
		// Add your logic here to retrieve a list of students based on the selected
		// class.
		List<String> studlist = studentRepo.findEmailAddressByClass(className);
		// System.out.println(studlist);
		List<StudentVideoWatchInf> studresult = studentVideoInfo.findStudentbyEmail(studlist);
		// System.out.println(studresult);
		// Generate an HTML table with the specified styles and serial numbers
		StringBuilder htmlTable = new StringBuilder();
		htmlTable.append("<table class=\"table\" style=\"background-color: rgb(205, 214, 224);\">");
		htmlTable.append("<thead class=\"thead-dark\">");
		htmlTable.append(
				"<tr class=\"heading-row\"><th>No.</th><th>Student Name</th><th>College</th><th>Percentage</th></tr>");
		htmlTable.append("</thead>");
		htmlTable.append("<tbody>");

		for (int i = 0; i < studresult.size(); i++) {
			StudentVideoWatchInf student = studresult.get(i);

			htmlTable.append("<tr>");
			htmlTable.append("<td>").append(i + 1).append("</td>"); // Serial number
			htmlTable.append("<td>").append(student.getEmail()).append("</td>");
			htmlTable.append("<td>").append(student.getCollege()).append("</td>");
			htmlTable.append("<td>").append(String.format("%.2f", student.getPercetageStatus())).append("%")
					.append("</td>");
			htmlTable.append("</tr>");
		}

		htmlTable.append("</tbody>");
		htmlTable.append("</table>");

		return htmlTable.toString();
	}

	@GetMapping("/AssignExerciseByTeacher")
	public ModelAndView assignExerciseByTeacher(ModelAndView modelAndView, Principal principal) {
		String email = principal.getName();
		// System.out.println(email);
		List<String> classes = createclassRepo.findClassNameByEmail(email);
		// System.out.println(classes);

		modelAndView.addObject("classes", classes);

		List<Exercise_Master> listExercise = exer_repo.findAll();
		modelAndView.addObject("listExercise", listExercise);
		modelAndView.setViewName("AssignExerciseByTeacher");
		return modelAndView;
	}

	@PostMapping("/AssignExerbyClass")
	public ModelAndView AssignExerciseByClass(ModelAndView modelAndView, Principal principal,
			@RequestParam("exer_name") String exer_name, @RequestParam("class_name") String class_name,
			@RequestParam("_csrf") String csrfToken, HttpServletRequest request, @RequestParam(value="hppCode") String hppCode) {

		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);

		if (csrf.getToken().equals(csrfToken)) {
			
			
			String[] params = new String[] {class_name,exer_name };
			System.out.println("hppcode backend = "+hppCode);
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				modelAndView.setViewName("error.html");
				
				return modelAndView;
			} 
			
			
			String email = principal.getName();

			String msg;
			List<String> classes = createclassRepo.findClassNameByEmail(email);
			// System.out.println(classes);

			modelAndView.addObject("classes", classes);

			List<Exercise_Master> listExercise = exer_repo.findAll();
			modelAndView.addObject("listExercise", listExercise);
			long n = exer_repo.findExidByProbstate(exer_name);
			
			if (!classExerRepo.checkClassExercise(class_name, n)) {
				ClassExercise classExercise = new ClassExercise();
				classExercise.setClass_name(class_name.replaceAll("\\<.*?\\>", ""));
				classExercise.setExer_name(exer_name.replaceAll("\\<.*?\\>", ""));

				classExercise.setExer_id(n);
				
				try {
				classExerRepo.save(classExercise);
				msg = "Exercise Assign to Class";
				modelAndView.addObject("msg", msg);
				}
				catch(DataIntegrityViolationException d) {
					msg = "Exercise Already Assigned to Class!";
					modelAndView.addObject("msg", msg);
				}
				catch(Exception e) {
					msg = "Some error occurred";
					modelAndView.addObject("msg",msg);
				}

			} else {
				msg = "Exercise Already Assigned to Class!";
				modelAndView.addObject("msg", msg);
			}

			modelAndView.setViewName("AssignExerciseByTeacher");
		} else {
			modelAndView.setViewName("error.html");
		}
		return modelAndView;
	}

	@GetMapping("/CreateClassByTeacher")
	public ModelAndView createClassByTeacher(ModelAndView modelAndView, Principal principal) {
		String email = principal.getName();
		TeacherDtls teacher = teacherRepo.findByEmailAddress(email);
		String colleges = teacher.getCollege();
		if (teacher != null) {

			// System.out.println(colleges);
			// Fetch students who have not been assigned a class
			List<StudentDtls> studentList = studentRepo.findCollegeBybatchByroleByclassname(0, true, "USER", colleges,
					"");
			// System.out.println("ans"+studentList);
			modelAndView.addObject("List", studentList);
		}

		List<String> classesByTeacher = createclassRepo.findClassNameByEmail(email);

		modelAndView.addObject("classesByTeacher", classesByTeacher);
		modelAndView.setViewName("CreateClassByTeacher.html");
		return modelAndView;
	}

	@ResponseBody
	@GetMapping("/checkUniqueclassName")
	public String checkUniqueclassName(@RequestParam("className") String className) {
		boolean exists = createclassRepo.existsByClassName(className);

		if (exists) {
			return "exists";
		} else {
			return "notExists";
		}
	}

	@PostMapping("/createClass")
	public String createClass(@RequestParam("className") String className, @RequestParam("students") String[] students,
			@RequestParam("teacherId") String teacherId, HttpSession session, RedirectAttributes redirectAttributes,
			Model model, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value="hppCode") String hppCode) {

		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
			
		String a="";
		
		for(String s:students)
		{
			a+=s;
		}
		
		
		if (csrf.getToken().equals(csrfToken)) {

			if (students == null || students.length == 0) {
				model.addAttribute("error", "No students selected.");
				return "redirect:/CreateClassByTeacher";
			}

			// Check if a class with the same name already exists
			if (createclassRepo.existsByClassName(className)) {
				session.setAttribute("success", "Class already exists!");
				return "redirect:/CreateClassByTeacher";
			}

			
			String[] params= new String[] {
					className,a,teacherId
			};
			
			if(!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
					return "error.html";
			} 
			
			// Fetch users with the USER role based on the selected students' IDs
			List<User> users = userRepo.findByUsernameInAndRole(Arrays.asList(students), "USER");
				
			// Update the class name and teacher ID for each user
			
			CreateClass createClass=new CreateClass();
			createClass.setClassName(className.replaceAll("\\<.*?\\>", ""));
			
			createClass.setTeacherId(teacherId);
			
			createclassRepo.save(createClass);
			redirectAttributes.addFlashAttribute("success", "Class created successfully!");
			// Update the className for each student and save it to StudentDtls table
			for (String studentEmail : students) {
				StudentDtls student = studentRepo.findByEmailAddress(studentEmail);
				if (student != null) {
					student.setClassName(className);
					studentRepo.save(student);
				}
			}

			return "redirect:/CreateClassByTeacher";
		}
		else {
			return "error.html";
		}

	}
	@RequestMapping("/assignClass")
	public String AssignClass(@RequestParam("className2") String className, @RequestParam("students") String[] students,
			HttpSession session, RedirectAttributes redirectAttributes, Model model,
			@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value="hppCode") String hppCode) {
			System.out.println(hppCode);
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
			System.out.println(className);
			String b="";
			
			for(String s:students)
			{
				b+=s;
			}
		
		String[] params= new String[] {
				className,b
		};
		
		if (csrf.getToken().equals(csrfToken)) {
			
			if(!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
					return "error.html";
			} 
			
			
			
			

			int count = studentRepo.findCountByClassName(className);

			if (count < 50) {
				// Update the className for each student and save it to StudentDtls table
				for (String studentEmail : students) {
					StudentDtls student = studentRepo.findByEmailAddress(studentEmail);
					if (student != null) {
						student.setClassName(className.replaceAll("\\<.*?\\>", ""));
						studentRepo.save(student);
					}
				}
				session.setAttribute("success1", "Student assign to class!");

			} else {
				session.setAttribute("error1", "Class limit exceed!");
			}

			return "redirect:/CreateClassByTeacher";
		} else {
			return "error.html";
		}
	}

	@GetMapping("/AccessBasicCourse")
	public ModelAndView AccessBasicCourse(ModelAndView modelandview) {
		modelandview.setViewName("AccessBasicCourse.html");

		return modelandview;

	}

	@GetMapping("/classexercisedetails")
	public ModelAndView classexerciseDetails(ModelAndView modelandview, Principal principal) {
		String email = principal.getName();
		List<String> className = createclassRepo.findClassNameByEmail(email);
		// System.out.println(className);
		List<ClassExercise> classExercises = classExerRepo.findByClass_nameIn(className);
		modelandview.addObject("classExercises", classExercises);
		modelandview.setViewName("Classexercise.html");

		return modelandview;

	}

}
