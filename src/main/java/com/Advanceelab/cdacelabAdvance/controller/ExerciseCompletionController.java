package com.Advanceelab.cdacelabAdvance.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.Advanceelab.cdacelabAdvance.dto.DataTable;
import com.Advanceelab.cdacelabAdvance.dto.Students;
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
   
	    return "Completionstatus1";
	}

	@GetMapping("/basicCompletionStatusData")
	public ResponseEntity<DataTable<StudentVideoWatchInf>> fetchBasicCompletionStatusData(
			@RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam(value = "search[value]", required = false, defaultValue = "") String searchTerm,
            @RequestParam(value = "order[0][column]", defaultValue = "0") int sortColumnIndex,
            @RequestParam(value = "order[0][dir]", defaultValue = "asc") String sortDirection) {

		String sortBy;
        switch (sortColumnIndex) {
        	case 0: sortBy = "id"; break;
        	case 1: sortBy = "id"; break;
            case 2: sortBy = "userId"; break;
            case 3: sortBy = "courseId"; break;
            case 4: sortBy = "courseName"; break;
            case 5: sortBy = "firstName"; break;
            case 6: sortBy = "lastName"; break;
            case 7: sortBy = "email"; break;
            case 8: sortBy = "college"; break;
            case 9: sortBy = "state"; break;
            case 10: sortBy = "batch"; break;
            case 11: sortBy = "percetageStatus"; break;
            default: sortBy = "id"; // Default sorting
        }
        
        Pageable pageable;
        
        int page = start / length;
        pageable = PageRequest.of(page, length, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        
        Page<StudentVideoWatchInf> responseData = studentVideoWatchInfRepo.searchBasicCompletion(searchTerm, "Basic", pageable);
        
        DataTable<StudentVideoWatchInf> dataTable = new DataTable<StudentVideoWatchInf>();
	    dataTable.setDraw(draw);
	    dataTable.setStart(start);
	    dataTable.setData(responseData.getContent());
	    dataTable.setRecordsTotal(responseData.getTotalElements());
	    dataTable.setRecordsFiltered(responseData.getTotalElements());

	    return ResponseEntity.ok(dataTable);
	}
	
	@GetMapping("/advanceCompletionStatusData")
	public ResponseEntity<DataTable<StudentVideoWatchInf>> fetchAdvanceCompletionStatusData(
			@RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam(value = "search[value]", required = false, defaultValue = "") String searchTerm,
            @RequestParam(value = "order[0][column]", defaultValue = "0") int sortColumnIndex,
            @RequestParam(value = "order[0][dir]", defaultValue = "asc") String sortDirection) {

		String sortBy;
        switch (sortColumnIndex) {
        	case 0: sortBy = "id"; break;
        	case 1: sortBy = "id"; break;
            case 2: sortBy = "userId"; break;
            case 3: sortBy = "courseId"; break;
            case 4: sortBy = "courseName"; break;
            case 5: sortBy = "firstName"; break;
            case 6: sortBy = "lastName"; break;
            case 7: sortBy = "email"; break;
            case 8: sortBy = "college"; break;
            case 9: sortBy = "state"; break;
            case 10: sortBy = "batch"; break;
            case 11: sortBy = "percetageStatus"; break;
            default: sortBy = "id"; // Default sorting
        }
        
        Pageable pageable;
        
        int page = start / length;
        pageable = PageRequest.of(page, length, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        
        Page<StudentVideoWatchInf> responseData = studentVideoWatchInfRepo.searchBasicCompletion(searchTerm, "Advance", pageable);
        
        DataTable<StudentVideoWatchInf> dataTable = new DataTable<StudentVideoWatchInf>();
	    dataTable.setDraw(draw);
	    dataTable.setStart(start);
	    dataTable.setData(responseData.getContent());
	    dataTable.setRecordsTotal(responseData.getTotalElements());
	    dataTable.setRecordsFiltered(responseData.getTotalElements());

	    return ResponseEntity.ok(dataTable);
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
		
		return "RegisteredStudents";
	}

	@GetMapping("/registeredStudentsData")
	public ResponseEntity<DataTable<Students>> fetchRegisteredStudentsData(
			@RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam(value = "search[value]", required = false, defaultValue = "") String searchTerm,
            @RequestParam(value = "order[0][column]", defaultValue = "0") int sortColumnIndex,
            @RequestParam(value = "order[0][dir]", defaultValue = "asc") String sortDirection) {

		String sortBy;
        switch (sortColumnIndex) {
        	case 0: sortBy = "id"; break;
            case 1: sortBy = "id"; break;
            case 2: sortBy = "firstName"; break;
            case 3: sortBy = "lastName"; break;
            case 4: sortBy = "fatherName"; break;
            case 5: sortBy = "qualification"; break;
            case 6: sortBy = "emailAddress"; break;
            case 7: sortBy = "mobileNumber"; break;
            case 8: sortBy = "dob"; break;
            case 9: sortBy = "category"; break;
            case 10: sortBy = "gender"; break;
            case 11: sortBy = "state"; break;
            case 12: sortBy = "college"; break;
            case 13: sortBy = "batch"; break;
            case 14: sortBy = "registrationDate"; break;
            case 15: sortBy = "approvedDate"; break;
            case 16: sortBy = "validTill"; break;
            default: sortBy = "id"; // Default sorting
        }
        
        Pageable pageable;
        
        int page = start / length;
        pageable = PageRequest.of(page, length, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        
        Page<Students> responseData = studentRepo.searchStudents(searchTerm, pageable);
        
        DataTable<Students> dataTable = new DataTable<Students>();
	    dataTable.setDraw(draw);
	    dataTable.setStart(start);
	    dataTable.setData(responseData.getContent());
	    dataTable.setRecordsTotal(responseData.getTotalElements());
	    dataTable.setRecordsFiltered(responseData.getTotalElements());

	    return ResponseEntity.ok(dataTable);
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