package com.Advanceelab.cdacelabAdvance.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.StudentVideoWatchInf;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentVideoWatchInfRepository;

@Controller
@RequestMapping
public class ExerciseCompletionController {

	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private StudentVideoWatchInfRepository studentVideoWatchInfRepo;

	@GetMapping("/ExerciseCompletion")
	public ModelAndView Exercisecompletionstatus(ModelAndView modelandview) {
		modelandview.setViewName("ExerciseCompletion.html");

		return modelandview;

	}
	@GetMapping("/Completionstatus")
	public ModelAndView Completionstatus(ModelAndView modelandview) {
		modelandview.setViewName("Completionstatus.html");

		return modelandview;

	}


	@GetMapping("/Completionstatus1")
	public String listexercisescom(Model model) {
		
		//List<StudentVideoWatchInf> studentBasic=studentVideoWatchInfRepo.findByCourseType("%Basic");
		List<StudentVideoWatchInf> studentBasic1=studentVideoWatchInfRepo.findByCourseType1("Basic");
//		for(StudentVideoWatchInf s1:studentBasic)
//		{
//			System.out.println(s1);
//		}
		
		
		model.addAttribute("studentBasic1", studentBasic1);
		
	//	List<StudentVideoWatchInf> studentAdvance=studentVideoWatchInfRepo.findByCourseType("%Advance");
		List<StudentVideoWatchInf> studentAdvance1=studentVideoWatchInfRepo.findByCourseType1("Advance");
//		for(StudentVideoWatchInf s2:studentAdvance)
//		{
//			System.out.println(s2);
//		}
		model.addAttribute("studentAdvance1", studentAdvance1);
		
   
	    return "Completionstatus1";
	}

	

//	@GetMapping("/Categorywise")
//	public String categorys(Model model) {
//
//		HashMap<String, Integer> category = studentRepo.getCategoryCountMap();
//
//		model.addAttribute("categorys", category);
//		model.addAttribute("pieData", category);
////		 System.out.println(category);
//
//		return "Categorywise";
//
//	}
	
	

	@GetMapping("/Categorywise")
	public String categorys(Model model) {
	    // Fetch category and gender counts from repository
	    HashMap<String, HashMap<String, Integer>> categoryGenderCounts = studentRepo.getCategoryGenderCountMap();

	    // Initialize a map to store total counts for each category
	    HashMap<String, Integer> categoryTotals = new HashMap<>();

	    // Variables to store overall totals
	    int totalMale = 0;
	    int totalFemale = 0;

	    // Calculate total counts for each category and overall totals
	    for (String category : categoryGenderCounts.keySet()) {
	        int maleCount = categoryGenderCounts.get(category).getOrDefault("Male", 0);
	        int femaleCount = categoryGenderCounts.get(category).getOrDefault("Female", 0);
	        categoryTotals.put(category, maleCount + femaleCount);
	        totalMale += maleCount;
	        totalFemale += femaleCount;
	    }

	    int totalStudents = totalMale + totalFemale;

	    // Add the calculated data to the model
	    model.addAttribute("categoryGenderCounts", categoryGenderCounts);
	    model.addAttribute("categoryTotals", categoryTotals);
	    model.addAttribute("totalMale", totalMale);
	    model.addAttribute("totalFemale", totalFemale);
	    model.addAttribute("totalStudents", totalStudents);
	    model.addAttribute("pieData", categoryTotals);

	    return "Categorywise";
	}

	
	////////

	@GetMapping("/Statewise")
	public String statewise(Model model) {
		HashMap<String, Integer> stateCountMap = studentRepo.getStateCountMap();
		//System.out.println(stateCountMap);

		model.addAttribute("state_names", stateCountMap);
		model.addAttribute("barData1", stateCountMap);

		return "Statewise";
	}

	@GetMapping("/download/{state}")
	public String downloadStudentDetlsByState(@PathVariable(value = "state") String state,Model model)
	{
		List<StudentDtls> student = studentRepo.findStudentDtlsByState(state);
		model.addAttribute("list", student);
		return "showStateWiseList";
	}
	
	
	@GetMapping("/downloadCategories/{categories}")
	public String downloadCategoriesWise(@PathVariable(value = "categories") String categories,Model model)
	{
		List<StudentDtls> student = studentRepo.findByCategories(categories);
		model.addAttribute("list", student);
		return "showCategoriesWiseList";
	}
	
	
	////////

	@GetMapping("/RegisteredStudents")
	public String RegisteredStudents(Model model) {
		List<StudentDtls> list = studentRepo.findByApproveAndRole(true,"USER");
		model.addAttribute("list", list);
		return "RegisteredStudents";
	}


//	@GetMapping("/StateandCategory")
//	public String getStatewiseancat(Model model) {
//	    List<Object[]> stateCategoryCounts = studentRepo.getStateCategoryCount(true,"USER");   
//	    model.addAttribute("stateCategory", stateCategoryCounts);
//	    return "StateandCategory";
//	}

	@GetMapping("/StateandCategory")
	public String getStatewiseancat(Model model) {
	    List<Object[]> stateCategoryCounts = studentRepo.getStateCategoryAndGenderCount(true, "USER");
	    model.addAttribute("stateCategory", stateCategoryCounts);
	    return "StateandCategory";
	}

	
}