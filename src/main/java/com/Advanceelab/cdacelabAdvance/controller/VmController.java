package com.Advanceelab.cdacelabAdvance.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Advanceelab.cdacelabAdvance.entity.Exercise_Master;
import com.Advanceelab.cdacelabAdvance.entity.Vm_Master;
import com.Advanceelab.cdacelabAdvance.repository.Exercise_MasterRepo;
import com.Advanceelab.cdacelabAdvance.repository.Vm_MasterRepo;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;

@Controller
public class VmController {

	
	@Autowired
	private Vm_MasterRepo vm_repo;
	
	@Autowired
	private Exercise_MasterRepo ex_repo;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@GetMapping("/addvm")
	public String showvm(Model model) {
		List<Exercise_Master> listExercise=ex_repo.findAll();
		model.addAttribute("vm",new Vm_Master());
		model.addAttribute("listExercise",listExercise);
	
		return "addvm";
	}
	
	@PostMapping("/savevm")
	public String addvm(@ModelAttribute Vm_Master vm, @RequestParam("_csrf") String csrfToken, HttpServletRequest request) {
	    CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
	    
	    String a=String.valueOf(vm.getExercise().getEx_id());
	    String b=a.replaceAll("\\<.*?\\>", "");
	    Long tpid=(long) Integer.parseInt(b);
	    
	    vm.getExercise().setEx_id(tpid);
	    vm.setVm_name(vm.getVm_name().replaceAll("\\<.*?\\>", ""));
	    vm.setVm_uuid(vm.getVm_uuid().replaceAll("\\<.*?\\>", ""));

	    String[] params = new String[]{vm.getVm_name(),vm.getVm_uuid(),a};
	    String hppCode = vm.getHppCode();

	   
	    if (csrf.getToken().equals(csrfToken)) {
	        if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            logger.info("HTTP Parameter pollution");
	            return "error.html";
	        } else {
	            // Check if Vm_id is null (new object) or not null (existing object)
	            if (vm.getVm_id() != null) {
	            	System.out.println("updating");
	                // Load the existing object from the repository
	                Optional<Vm_Master> existingVmOptional = vm_repo.findById(vm.getVm_id());
	                if (existingVmOptional.isPresent()) {
	                    // Update the existing object with values from the form
	                    Vm_Master existingVm = existingVmOptional.get();
	                    existingVm.setExercise(vm.getExercise());
	                    existingVm.setVm_name(vm.getVm_name());
	                    existingVm.setVm_uuid(vm.getVm_uuid());
	                    // Add more properties as needed

	                    // Save the updated object
	                    vm_repo.save(existingVm);
	                } else {
	                    // Handle the case where the existing object is not found
	                    return "error.html";
	                }
	            } else {
	            	System.out.println("creating");
	                // Save the new object
	                vm_repo.save(vm);
	            }

	            return "alert2.html";
	        }
	    }

	    return "error.html";
	}
	
	
	
	@PostMapping("/updateVm")
	public String updateVm(@ModelAttribute Vm_Master vm, @RequestParam("_csrf") String csrfToken, HttpServletRequest request) {
	    CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
	    
	    String a=String.valueOf(vm.getExercise().getEx_id());
	    String b=a.replaceAll("\\<.*?\\>", "");
	    Long tpid=(long) Integer.parseInt(b);
	    
	    String vmid=String.valueOf(vm.getVm_id());
	    
	    vm.getExercise().setEx_id(tpid);
	    vm.setVm_name(vm.getVm_name().replaceAll("\\<.*?\\>", ""));
	    vm.setVm_uuid(vm.getVm_uuid().replaceAll("\\<.*?\\>", ""));

	    String[] params = new String[]{vm.getVm_name(),vm.getVm_uuid(),a,vmid};
	    String hppCode = vm.getHppCode();

	   
	    if (csrf.getToken().equals(csrfToken)) {
	        if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            logger.info("HTTP Parameter pollution");
	            return "error.html";
	        } else {
	            // Check if Vm_id is null (new object) or not null (existing object)
	            if (vm.getVm_id() != null) {
	            	System.out.println("updating");
	                // Load the existing object from the repository
	                Optional<Vm_Master> existingVmOptional = vm_repo.findById(vm.getVm_id());
	                if (existingVmOptional.isPresent()) {
	                    // Update the existing object with values from the form
	                    Vm_Master existingVm = existingVmOptional.get();
	                    existingVm.setExercise(vm.getExercise());
	                    existingVm.setVm_name(vm.getVm_name());
	                    existingVm.setVm_uuid(vm.getVm_uuid());
	                    // Add more properties as needed

	                    // Save the updated object
	                    vm_repo.save(existingVm);
	                } else {
	                    // Handle the case where the existing object is not found
	                    return "error.html";
	                }
	            } else {
	            	System.out.println("creating");
	                // Save the new object
	                vm_repo.save(vm);
	            }

	            return "alert4.html";
	        }
	    }

	    return "error.html";
	}
	
	
	
	
	@GetMapping("/vmdetails")
	public String vmdetails(Model model) 
	{ 
		List<Vm_Master> listvm=vm_repo.findAll();
		model.addAttribute("listvm", listvm);
       return "vmdetails";
	}  
	
	

	@GetMapping("/showupdatevm")
	public String showupdatevm(@RequestParam Long vms_id, Model model) {
	    List<Exercise_Master> listExercise = ex_repo.findAll();
	    model.addAttribute("listExercise", listExercise);

	    try {
	        Optional<Vm_Master> vm_update = vm_repo.findById(vms_id);
	        Vm_Master vm = vm_update.orElseThrow(() -> new Exception("Unable to find object by given id"));

	        model.addAttribute("vm", vm);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return "vmupdate";
	}
	
	@PostMapping("/deletevm/{Vm_id}")
    public String deletevm(@PathVariable(name="Vm_id") Long Vm_id,Model model,@RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
		CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
		if (csrf.getToken().equals(csrfToken)) {
			String a=String.valueOf(Vm_id);
			String[] params = new String[] { a };
					
			if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            logger.info("HTTP Parameter pollution");
	            // Render the error page and include it in the response
	            return "error.html";
	        }
		}
    	vm_repo.deleteById(Vm_id);
    	return "redirect:/vmdetails";
    }
	
	
}
