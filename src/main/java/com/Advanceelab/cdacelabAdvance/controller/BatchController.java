package com.Advanceelab.cdacelabAdvance.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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
import org.springframework.web.servlet.ModelAndView;

import com.Advanceelab.cdacelabAdvance.entity.Batch_Master;
import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.TeacherDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.entity.Vm_Master;
import com.Advanceelab.cdacelabAdvance.repository.Batch_Master_repo;
import com.Advanceelab.cdacelabAdvance.repository.Exercise_MasterRepo;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.TeacherRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.repository.Vm_MasterRepo;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;
import java.util.logging.Logger;

@Controller
@RequestMapping
public class BatchController {

	private final int MAX_COUNT_IN_BATCH = 50;

	@Autowired
	private Exercise_MasterRepo exer_repo;

	@Autowired
	private Batch_Master_repo batch_masterRepo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TeacherRepository teacherRepo;
	
	@Autowired
	private Vm_MasterRepo vm_MasterRepo;

	private Logger logger = Logger.getLogger(getClass().getName());
	
	@GetMapping("/Batchexercise")
	public ModelAndView AssignExercisee(Model model) {
		ModelAndView mav = new ModelAndView("Batchexercise");
		List<Batch_Master> batchexercise = batch_masterRepo.findAll();

		model.addAttribute("batchexercise", batchexercise);
		return mav;

	}

	
	@PostMapping("delexerbatch/{batch_id}")
	public String deleteexercise(@PathVariable(name = "batch_id") Integer batch_id,HttpSession session,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode ) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(batch_id);
			String[] params = new String[] { a };
					
					if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
						logger.info("HTTP Parameter pollution");

						return "error.html";
					}
		}
		batch_masterRepo.deleteById(batch_id);
		return "redirect:/Batchexercise";
	}

	@GetMapping("/updatebatchex")
	public ModelAndView showupdateexer(@RequestParam Integer batch_id, Model model) {
		ModelAndView mav = new ModelAndView("batchexupdate");
		List<Exercise_Master> listExercise = exer_repo.findAll();

		model.addAttribute("listExercise", listExercise);
		List<User> listbatch = userRepo.findAll();
		
		
		Set<Integer> b = new HashSet<>();
		for (User u : listbatch) {
		    b.add(u.getBatch());
		}

		model.addAttribute("batch_id", batch_id);
		model.addAttribute("listbatch", b);

		try {
			Optional<Batch_Master> batchex_update = batch_masterRepo.findById(batch_id);
			if (batchex_update != null) {
				mav.addObject("batchex_update", batchex_update);

			} else {
				throw new Exception("Unable to find object by given id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

//	 
	@GetMapping("/assignExercise")
	public ModelAndView assignExercisee(Model model) {
		ModelAndView mav = new ModelAndView("AssignExercise");
		List<Exercise_Master> listExercise = exer_repo.findAll();
		List<Integer> listbatch = studentRepo.findListofBatch();
//	    	System.out.print(listbatch);
		Set<Integer> a = new HashSet<Integer>();
		for (Integer u : listbatch) {
			a.add(u);
		}
		a.remove(0);
//	    	System.out.println("kedfqebifdebvgif");
		// System.out.println(a);
		model.addAttribute("listExercise", listExercise);
		model.addAttribute("listbatch", a);
		return mav;

	}
	
	@PostMapping("/savebatchex")
	public ModelAndView batchex(@ModelAttribute @Valid Batch_Master batch_master, Model model,
			@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value="hppCode") String hppCode) {
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		ModelAndView mav = new ModelAndView();
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(batch_master.getBatch_no());
			String b=a.replaceAll("\\<.*?\\>", "");
			String ex_idd=String.valueOf(batch_master.getExer_id());
			batch_master.setExer_name(batch_master.getExer_name().replaceAll("\\<.*?\\>", ""));
			String[] params = new String[] { batch_master.getExer_name(),a,ex_idd };
	
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");

				mav.setViewName("error.html");
				return mav;
			} 
			
			String msg;
			long exid = exer_repo.findExidByExer_name(batch_master.getExer_name());
			List<Vm_Master> vm_Masters = vm_MasterRepo.findAllByExerciseId(exid);
			if(vm_Masters.isEmpty() || vm_Masters == null)
				model.addAttribute("msg", "Please assign at least one virtual machine to that exercise.");
			else if (!batch_masterRepo.checkExercise(batch_master.getBatch_no(), (int)exid)) {
				msg = "Exercise Assigned to Batch";
				batch_master.setExer_id((int)exid);
				batch_masterRepo.save(batch_master);
				model.addAttribute("msg", msg);
			} else {
				msg = "Exercise Already Assigned to Batch";
				model.addAttribute("msg", msg);
			}

			mav.setViewName("Batchexercise");
			List<Batch_Master> batchexercise = batch_masterRepo.findAll();
			model.addAttribute("batchexercise", batchexercise);
		} else {
			mav.setViewName("error.html");
		}
		return mav;

	}
	

	
	@PostMapping("/updateSavebatchex")
	public ModelAndView updatebatchex(@ModelAttribute @Valid Batch_Master batch_master, Model model,
			@RequestParam("_csrf") String csrfToken, HttpServletRequest request, @RequestParam(value="batch_id") Integer batch_id) {
		
		System.out.println("above");
		ModelAndView mav = new ModelAndView();
		String msg;
		if(batch_masterRepo.findById(batch_id) == null)
		{
			msg = "batch Id  is not available.";
			model.addAttribute("msg", msg);
			mav.setViewName("Batchexercise");
			
			System.out.println("inside");
			
			return mav;
		}
		
		System.out.println("outside");
		
		
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		
		
		batch_master.setExer_name(batch_master.getExer_name().replaceAll("\\<.*?\\>", ""));

		String[] params = new String[] { batch_master.getExer_name() };
		String hppCode = batch_master.getHppCode();
		
		if (csrf.getToken().equals(csrfToken)) {
			
			
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
				
				Batch_Master batch_Master2 = new Batch_Master();
				batch_Master2.setBatch_id(batch_id);
				batch_Master2.setBatch_no(batch_master.getBatch_no());
				batch_Master2.setExer_id(batch_master.getExer_id());
				batch_Master2.setExer_name(batch_master.getExer_name());
				
				batch_masterRepo.save(batch_Master2);
				
				mav.setViewName("error.html");
				return mav;
			} 
			
			
			
			int exid = (int) exer_repo.findExidByExer_name(batch_master.getExer_name());
			if (!batch_masterRepo.checkExercise(batch_master.getBatch_no(), exid)) {
				msg = "Exercise Assigned to Batch";

				batch_master.setExer_id(exid);
				batch_masterRepo.save(batch_master);
				model.addAttribute("msg", msg);
			} else {
				msg = "Exercise Already Assigned to Batch";
				model.addAttribute("msg", msg);
			}

			mav.setViewName("Batchexercise");
			

		
		} else {
			mav.setViewName("error.html");
		}
		return mav;

	}

	@GetMapping("/AssignBatch")
	public String assignBatch(Model model) {

		List<StudentDtls> studentsWithoutClassNameAndBatch = studentRepo.findByApprovedAndClassNameIsNullAndBatchEqualsAndRoleEquals(0, true, "", "USER");

		List<TeacherDtls> approvedTeachersWithSameCollege = teacherRepo.findByApprovedAndCollegeIn(true,
												studentsWithoutClassNameAndBatch.stream().map(StudentDtls::getCollege).collect(Collectors.toList()));

		model.addAttribute("studentsWithoutClassNameAndBatch", studentsWithoutClassNameAndBatch);
		model.addAttribute("teachersWithSameCollege", approvedTeachersWithSameCollege);

		Integer currentBatch = 0;
		currentBatch = studentRepo.findCurrentBatchNumber();

		if (currentBatch == null) {
			return "AssignBatch";
		}

		if (currentBatch == 0)
			currentBatch = 1;

		int numberOfStudentsInCurrentBatch = studentRepo.countByBatch(currentBatch);
		
		System.out.println("numberOfStudentsInCurrentBatch : "+numberOfStudentsInCurrentBatch);

		if (numberOfStudentsInCurrentBatch >= MAX_COUNT_IN_BATCH) {
			currentBatch += 1;
			numberOfStudentsInCurrentBatch = 0;
		}

		int remainingSlots = MAX_COUNT_IN_BATCH - numberOfStudentsInCurrentBatch;
		model.addAttribute("remainingSlots", remainingSlots); // Add remaining slots to the model
		model.addAttribute("currentBatch", currentBatch); // Add remaining slots to the model
		return "AssignBatch"; // Name of the HTML template to render the data
	}

	@PostMapping("/assignBatch")
	@Transactional
	public String assignBatch(@RequestParam("batch") @Min(1) int batch, @RequestParam("studentId") int studentId,
			@RequestParam(value = "emailAddress", required = false) String emailAddress, Model model,
			HttpSession session, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value="hppCode") String hppCode) {

		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
				
		if (csrf.getToken().equals(csrfToken)) {
			
			String a=String.valueOf(batch);
			
			String b=a.replaceAll("\\<.*?\\>", "");
			String c=String.valueOf(studentId);
			String d=a.replaceAll("\\<.*?\\>", "");
						
			String[] params = new String[] { a,c };
				
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
				logger.info("HTTP Parameter pollution");
				return "error.html";
			} 
			
			
		      try {
		    	  Optional<StudentDtls> studentOptional = studentRepo.findById(studentId);

					if (studentOptional.isPresent()) {
						StudentDtls student = studentOptional.get();
						student.setBatch(batch);
						studentRepo.save(student);

						// Update the User entity batch field here
						if (student.getEmailAddress() != null) {
							User user = userRepo.findByEmailAddress(student.getEmailAddress());
							if (user != null) {
								user.setBatch(batch);
								userRepo.save(user);
							}
						}
		                session.setAttribute("assign", "Batch assigned successfully.");
		                return "redirect:/AssignBatch";
		            } else {
		                return "error.html";
		            }

		        } catch (OptimisticLockingFailureException ex) {
		            logger.info("Concurrency issue occurred: {}" +ex.getMessage());
		            return "error.html";
		        }
		}			
		return "error.html";
	}

}