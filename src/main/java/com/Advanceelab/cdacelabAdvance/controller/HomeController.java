package com.Advanceelab.cdacelabAdvance.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.Advanceelab.cdacelabAdvance.Captcha.GenerateCaptcha;
import com.Advanceelab.cdacelabAdvance.entity.AdvanceLabUserVmDetails;
import com.Advanceelab.cdacelabAdvance.entity.ExerciseContent;
import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;
import com.Advanceelab.cdacelabAdvance.entity.ReleaseNote1;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.StudentTrackTime;
import com.Advanceelab.cdacelabAdvance.entity.TeacherDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.entity.Vm_Master;
import com.Advanceelab.cdacelabAdvance.repository.AdvanceLabUserVmDetailsRepository;
import com.Advanceelab.cdacelabAdvance.repository.Batch_Master_repo;
import com.Advanceelab.cdacelabAdvance.repository.ClassExerciseRepository;
import com.Advanceelab.cdacelabAdvance.repository.ExerciseContentRepository;
import com.Advanceelab.cdacelabAdvance.repository.ExerciseSubmission_Repo;
import com.Advanceelab.cdacelabAdvance.repository.Exercise_MasterRepo;
import com.Advanceelab.cdacelabAdvance.repository.ReleaseNote1Repo;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentTrackTimeRepository;
import com.Advanceelab.cdacelabAdvance.repository.TeacherRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.repository.Vm_MasterRepo;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;
import com.Advanceelab.cdacelabAdvance.service.AdvanceLabService;
import com.Advanceelab.cdacelabAdvance.service.GuacamoleService;

import java.util.Optional;

@Controller
@RequestMapping
public class HomeController {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private Exercise_MasterRepo exercise_MasterRepo;
	
	@Autowired
	private UserRepository userRepo;
		
	@Autowired
	private Batch_Master_repo batchmasterRepo;

	@Autowired
	private GenerateCaptcha generateCaptcha;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private com.Advanceelab.cdacelabAdvance.mailSender.EmailSenderService senderService;

	@Autowired
	private ClassExerciseRepository classexerciseRepo;

	@Autowired
	private TeacherRepository teacherRepo;
	
	@Autowired
    private AdvanceLabService advanceLabService;
	
	@Autowired
	private ExerciseContentRepository exerciseContentRepository;
	
	@Autowired
	private AdvanceLabUserVmDetailsRepository advanceLabUserVmDetailsRepository;
	
	@Autowired
	private Vm_MasterRepo vm_MasterRepo;
	
	@Autowired
	private StudentTrackTimeRepository studentTrackTimeRepo;
	
	@Autowired
	private ReleaseNote1Repo releaseNote1Repo;
	
	@Autowired
	private GuacamoleService guacamoleService;
	
	@Autowired
	private ExerciseSubmission_Repo exerciseSubmission_Repo;

	@GetMapping("/accept/{id}")
	public String sendMail(@PathVariable(value = "id") int id, Model m, HttpSession session) {
		Optional<StudentDtls> studentDtls = studentRepo.findById(id);
		StudentDtls user = studentDtls.get();
		m.addAttribute("studentDtls", user);
		senderService.sendEmail(user.getEmailAddress(), "this is subject", "this is body of email");
		session.setAttribute("msg", "message sent....");
		return "redirect:/";
	}

	@GetMapping("/welcome")
	public ModelAndView home() {
		List<ReleaseNote1> notes = releaseNote1Repo.findAll();
		return new ModelAndView("welcome","notes",notes);
	}

	@GetMapping("/ConfigVPN")
	public ModelAndView ConfigVPN(ModelAndView modelandview) {
		modelandview.setViewName("ConfigVPN.html");
		return modelandview;
	}

	@GetMapping("/AccessLAB")
	public ModelAndView accesslab(ModelAndView modelandview) {
		modelandview.setViewName("AccessLAB.html");
		return modelandview;
	}

	@GetMapping("/access-denied")
	public String accessDenied() {
		return "redirect:/login?accessDenied";
	}

	private boolean isUserAuthenticated() {
		return false;
	}

	@GetMapping("/login")
	public String login(Model model, HttpSession session,
			@RequestParam(value = "error", required = false) String error) {

		String challenge = generateCaptcha.generateCaptchaChallenge();
		session.setAttribute("captchaChallenge", challenge);
		BufferedImage captchaImage = GenerateCaptcha.generateImage(challenge);
		String base64Image = imageToBase64(captchaImage);
		model.addAttribute("captchaImage", base64Image);
		if (error != null) {
			model.addAttribute("loginErrorMessage", "Invalid Credentials or Captcha. Please try again.");
		}
		if (isUserAuthenticated()) {
			return "redirect:/dashboard";
		}
		return "login";
	}
	
	@GetMapping("/refreshCaptcha")
	@ResponseBody
	public Map<String, String> refreshCaptcha(HttpSession session, Model model) {
	    Map<String, String> response = new HashMap<>();
	    String challenge = generateCaptcha.generateCaptchaChallenge();
	    session.setAttribute("captchaChallenge", challenge);
	    BufferedImage captchaImage = GenerateCaptcha.generateImage(challenge);
	    String base64Image = imageToBase64(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
	    response.put("captchaChallenge", challenge);
	    response.put("base64Image", base64Image);
	    return response;
	}

	@GetMapping("/checkApprovalStatus")
	@ResponseBody
	public Map<String, Object> checkApprovalStatus(@RequestParam("email") String email) {
		Map<String, Object> result = new HashMap<>();

		StudentDtls student = null;

		TeacherDtls teacher = null;
		if (userRepo.findByEmailAddress(email) != null) {
			result.put("status", "approved");
			return result;
		}
		if (studentRepo.findByLabemail(email) != null) {
			student = studentRepo.findByLabemail(email);
			if (student.isApproved()) {
				result.put("status", "approved");
			} else if (!student.isApproved()) {
				result.put("status", "not approved");
			}
		} else if (teacherRepo.findByEmailAddress(email) != null) {
			teacher = teacherRepo.findByEmailAddress(email);
			if (teacher.isApproved()) {
				result.put("status", "approved");
			} else if (!teacher.isApproved()) {
				result.put("status", "not approved");
			}
		} else {
			result.put("status", "not registered");
		}

		return result;
	}

	@PostMapping("/login")
	public String login(@ModelAttribute User user, @RequestParam("_csrf") String csrfToken,
			HttpServletRequest request) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		user.setEmailAddress(user.getEmailAddress().replaceAll("\\<.*?\\>", ""));
		user.setPassword(user.getPassword().replaceAll("\\<.*?\\>", ""));
		String[] params = new String[] { user.getEmailAddress(), user.getPassword() };
		String hppCode = user.getHppCode();

		if (csrf.getToken().equals(csrfToken)) {
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				return "error.html";
			} else {
				return "/";
			}
		}
		return "error.html";
	}

	private String imageToBase64(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] imageBytes = baos.toByteArray();
		return Base64.getEncoder().encodeToString(imageBytes);
	}

	@GetMapping("/")
	@Transactional
	public ModelAndView home(ModelAndView modelandview, Authentication authentication, Model model) throws Exception {

		Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null) {
			List<ReleaseNote1> notes = releaseNote1Repo.findAll();
			model.addAttribute("notes",notes);
			modelandview.setViewName("welcome");
			return modelandview;
		}
		Collection<? extends GrantedAuthority> authorities = authentication1.getAuthorities();
		String role = authorities.stream().map(GrantedAuthority::getAuthority).findFirst().orElse("");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userlogin;
		if (principal instanceof UserDetails) {
			userlogin = ((UserDetails) principal).getUsername();
		} else {
			userlogin = principal.toString();
		}
		if (role.equals("ADMIN")) {
		
			modelandview.setViewName("AdminHome.html");
		} else if (role.equals("TEACHER")) {
			
			User user=userRepo.findByUsername(userlogin);
        	user.setLoginAttempt(0);
        	user.setLoginTime(null);
        	user.setApproved(true);
        	userRepo.save(user);
			
			modelandview.setViewName("TeacherHome.html");
		} else {

			User user=userRepo.findByUsername(userlogin);
        	user.setLoginAttempt(0);
        	user.setLoginTime(null);
        	user.setApproved(true);
        	userRepo.save(user);
			
        	StudentTrackTime studentTrackTime = studentTrackTimeRepo.findByUsername(userlogin);
        	if(studentTrackTime != null)
        	{
    			studentTrackTime.setLastloginTime(ZonedDateTime.now());
    			studentTrackTimeRepo.save(studentTrackTime);
        	}
        	else
        	{
        		StudentTrackTime studentTrackTime1 = new StudentTrackTime();
        		studentTrackTime1.setUsername(userlogin);
        		studentTrackTime1.setFirstloginTime(ZonedDateTime.now());
        		studentTrackTime1.setLastloginTime(ZonedDateTime.now());
        		studentTrackTimeRepo.save(studentTrackTime1);
        	}
			modelandview.setViewName("home.html");
		}

		return modelandview;
	}
	
	private static String getUsername() {
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    return (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
	}

	@GetMapping("/exercisepage")
	public String exercisePage(Model model) {
		
		String labemail = getUsername();

		StudentDtls studentDtls = studentRepo.findByLabemail(labemail);
		String classname = studentDtls.getClassName();
		List<Long> submittedExercises = exerciseSubmission_Repo.findExerciseIdByUsername(labemail);
			model.addAttribute("submittedExercises", submittedExercises);
		if (classname != null && !classname.isEmpty()) {
			List<Long> exercisesId = classexerciseRepo.findExerIdbyClassName(classname);
			List<Exercise_Master> exerciseslist = exercise_MasterRepo.findByEx_idIn(exercisesId);
			model.addAttribute("exerciseslist", exerciseslist);
		} else {
			int i = studentDtls.getBatch();
			List<Integer> exercisesId = batchmasterRepo.findExerIdByBatchNo(i);
			List<Long> longList = exercisesId.stream().map(Long::valueOf).toList();
			List<Exercise_Master> exerciseslist = exercise_MasterRepo.findByEx_idIn(longList);
			model.addAttribute("exerciseslist", exerciseslist);
		}

		return "exercisepage";
	}

	@GetMapping("/exercise/{base64ExerciseId}")
    public String getString(Model model, @PathVariable String base64ExerciseId, HttpSession session)
            throws IOException, InterruptedException, ExecutionException {
	    String message = (String) session.getAttribute("msg");
	    if (message != null) {
	        model.addAttribute("message", message);
	        session.removeAttribute("msg");
	    }
	    byte[] byteExerciseId = Base64.getDecoder().decode(base64ExerciseId);
        String decodedString = new String(byteExerciseId);
        String id = decodedString.replace("CRnsQ2EtM8sIXwXIDcA8HhSYa", "").replace("3vR3mTRMZuemwSlFt4LRXRV62", "");
        long exerciseId = Long.parseLong(id);
        String labemail = getUsername();
        ExerciseContent exerciseContent = exerciseContentRepository.findByExerciseId((int)(exerciseId));
        
        if (exerciseContent != null) {
            boolean submitted = advanceLabService.checkExerciseSubmitted(labemail,exerciseId);
            boolean AtLeastOneVmNotCreated = false;
            List<AdvanceLabUserVmDetails> advanceLabUserVmDetails = new ArrayList<>();
            String userToken = null;
            if(submitted == false)
            {
            	advanceLabUserVmDetails = advanceLabService.checkVmAllReadyCreated(exerciseId, labemail);
            	
            	String username = labemail.substring(0, labemail.indexOf('@'));
            	StudentDtls studentDtls = studentRepo.findByLabemail(labemail);
                String hcipassword = advanceLabService.generateHciPassword(studentDtls.getFirstName(),studentDtls.getDob());
            	String authToken = guacamoleService.getAdminToken().get();
                boolean userFound = guacamoleService.checkDetailsOfUser(username, authToken).get();
                
                if(!userFound && !advanceLabUserVmDetails.isEmpty()) {
                	System.out.println("User not found in Guacamole, but VM for the exercise is present; proceeding to delete the VM.");
                	
	                for(AdvanceLabUserVmDetails vm: advanceLabUserVmDetails) {
	                	
	                	String connectionIdentifier = guacamoleService.getConnectionIdentifier(vm.getVmName(), authToken).get();
	                	guacamoleService.deleteConnection(connectionIdentifier, authToken);
	                	advanceLabService.deleteAdvanceLabVM(labemail, exerciseId);
	                }
                }
                
                AtLeastOneVmNotCreated = advanceLabService.checkAtLeastOneVmNotCreated(exerciseId, labemail);
                advanceLabUserVmDetails = advanceLabService.checkVmAllReadyCreated(exerciseId, labemail);
                
            	//Generating user token
            	if(!advanceLabUserVmDetails.isEmpty()) {
       
	            	userToken = guacamoleService.getUserToken(username, hcipassword).get();
	        		userToken = Base64.getEncoder().encodeToString(userToken.getBytes());
	        		model.addAttribute("token", userToken);
            	}
            }
            
            model.addAttribute("AtLeastOneVmNotCreated", AtLeastOneVmNotCreated);
            model.addAttribute("advanceLabUserVmDetails", advanceLabUserVmDetails);
            model.addAttribute("exerciseContent", exerciseContent);
            model.addAttribute("submitted", submitted);
            //Adding token in the model attribute
            return "StudentExercisePage";
        } else {
            return "error.html";
        }
    }
	
	@PostMapping("/launchConsole")
	public ModelAndView launchConsole(@RequestParam ("vmName") String vmName, @RequestParam ("token") String userToken) throws InterruptedException, ExecutionException {

    	String url = guacamoleService.getVMUrl(vmName);
    	
    	byte[] userTokenBytes = Base64.getDecoder().decode(userToken);

    	String token = new String(userTokenBytes);
    	
		url = url + "?token=" + token;
		return new ModelAndView("console", "url", url);
	}
	
	@PostMapping("/createAdvanceLab")
	public String createAdvanceLab(@RequestParam(value="createLabexerciseId") long exerciseId, @RequestParam(value="hppCodeCreateLab") String hppCodeCreateLab)
									throws InterruptedException, ExecutionException
	{
		String validation = advanceLabService.checkInputFeildValidation(exerciseId, hppCodeCreateLab);
		if(validation.equals("error"))
			return "error.html";
		String labemail = getUsername();
		String password = advanceLabService.generateUserPass(labemail);
		List<Vm_Master> AllVmOfTheExercise = vm_MasterRepo.findAllByExerciseId(Long.valueOf(exerciseId));
		for(Vm_Master vm:AllVmOfTheExercise)
		{
			String vmName = advanceLabService.generateVmName(vm.getVm_name(),labemail);
			Set<String> AllReadyCreatedVm = advanceLabUserVmDetailsRepository.findByUsername(labemail, exerciseId);
			
			if(AllReadyCreatedVm.contains(vmName))
				continue;
			
			advanceLabService.cloneVm(vm.getVm_uuid(), vmName, password);
			
			CompletableFuture<String> clonedVmUUID = advanceLabService.searchUuid(vmName,password);
			CompletableFuture<String> clonedVmIp = advanceLabService.searchIp(vmName, password);
			for(int i=0;i<10;i++)
			{
				System.out.println("UUID Not found");
				if(clonedVmUUID.get() != null)
					break;
				
				clonedVmUUID = advanceLabService.searchUuid(vmName,password);
				TimeUnit.SECONDS.sleep(5);
			}
			for(int i=0;i<10;i++)
			{
				System.out.println("IP Not found");
				if(clonedVmIp.get() != null)
					break;
				
				clonedVmIp = advanceLabService.searchIp(vmName, password);
				TimeUnit.SECONDS.sleep(5);
			}
			System.out.println("UUID : " + clonedVmUUID.get() +" ip : " + clonedVmIp.get());
			if(clonedVmUUID.get() != null && clonedVmIp.get() != null && !AllReadyCreatedVm.contains(vmName))
			{
				advanceLabService.save(labemail, vmName, clonedVmUUID.get(), clonedVmIp.get(),exerciseId);
				advanceLabService.powerOn(clonedVmUUID.get(), password);
				TimeUnit.SECONDS.sleep(15);
				
				//Guacamole Code for user creation
            	String authToken = guacamoleService.getAdminToken().get();
            	StudentDtls studentDtls = studentRepo.findByLabemail(labemail);
        		String fullName = studentDtls.getFirstName()+" "+studentDtls.getLastName();
        		String username = null;
        		String hcipassword = null;
        		if (authToken !=null) {
        			username = labemail.substring(0, labemail.indexOf('@'));
        			boolean userFound = guacamoleService.checkDetailsOfUser(username, authToken).get();
        			if(!userFound) {
        				hcipassword = advanceLabService.generateHciPassword(studentDtls.getFirstName(),studentDtls.getDob());
        				String response = guacamoleService.createUser(username, hcipassword, authToken, fullName).get();
        				//System.out.println(hcipassword);
        				if(response != null) {
        					System.out.println(response);
        				} else {
        					System.out.println("User not created");
        				}
        			}
        		}
        		
        		//Create connection in guacamole & assign the connection to the user
				String connectionIdentifier = null;
				String hostname = clonedVmIp.get();
				
				String response = guacamoleService.createConnection(vmName, hostname, authToken).get();
				System.out.println(response);
				if(response.equals("RDP connection created in guacamole.") || response.equals("RDP connection already exists.")) {
					connectionIdentifier = guacamoleService.getConnectionIdentifier(vmName, authToken).get();
					if(connectionIdentifier != null) {
						//System.out.println(connectionIdentifier);
						boolean assigned = guacamoleService.assignConnection(username, authToken, connectionIdentifier).get();
						if(assigned) {
							System.out.println("Connection assigned to the user");
						} else {
							System.out.println("Connection not assigned");
						}
					} else {
						System.out.println("Machine Identifier not found");
					}
				}
				else {
					System.err.println("Connection creation failed");
				}
			}
			else { 
				if(clonedVmUUID.get() != null) 
				{
					advanceLabService.deleteVm(clonedVmUUID.get());
					TimeUnit.SECONDS.sleep(10);
				}
			}
		}
			
		Optional<Exercise_Master> exId = exercise_MasterRepo.findById(exerciseId);
		long exerId = exId.get().getEx_id();
		String key = "CRnsQ2EtM8sIXwXIDcA8HhSYa" + exerId + "3vR3mTRMZuemwSlFt4LRXRV62";
		String encodedExId = Base64.getEncoder().encodeToString(key.getBytes());
		return "redirect:/exercise/" + encodedExId;
	}
	
	
	@PostMapping("/deleteAdvanceLabVM")
	public String deleteAdvanceLabVM(@RequestParam(value="deleteLabexerciseId") long exerciseId,@RequestParam("submissionPdf") MultipartFile submissionPdf, 
									 HttpServletRequest request, HttpSession session,@RequestParam(value = "hppCodeDeleteLabVm") String hppCodeDeleteLabVm) throws InterruptedException, IOException, ExecutionException
	{
		String validation = advanceLabService.checkInputFeildValidationForDeleteVm(exerciseId, submissionPdf.getOriginalFilename(), hppCodeDeleteLabVm);
		if(validation.equals("error"))
			return "error.html";
		
		String username = getUsername();
		User user = userRepo.findByUsername(username);
		int userId = user.getId();
		String pdfMessage = advanceLabService.saveAdvanceLabUserExercise(username,userId,exerciseId,submissionPdf);
		if(pdfMessage.equals("valid")) {
			Set<String> allVMName = advanceLabUserVmDetailsRepository.findByUsername(username, exerciseId);
			
			advanceLabService.deleteAdvanceLabVM(username,exerciseId);
			
			String authToken = guacamoleService.getAdminToken().get();
			
			for (String vm: allVMName) {
				String connectionIdentifier = guacamoleService.getConnectionIdentifier(vm, authToken).get();
				boolean status = guacamoleService.deleteConnection(connectionIdentifier, authToken).get();
				System.out.println(status);
			}
		}
		else {
			session.setAttribute("msg","Your file is malicious.");
		}	
		Optional<Exercise_Master> exId = exercise_MasterRepo.findById(exerciseId);
		long exerId = exId.get().getEx_id();
		String key = "CRnsQ2EtM8sIXwXIDcA8HhSYa" + exerId + "3vR3mTRMZuemwSlFt4LRXRV62";
		String encodedExId = Base64.getEncoder().encodeToString(key.getBytes());
		return "redirect:/exercise/" + encodedExId;
	}
}