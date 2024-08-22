package com.Advanceelab.cdacelabAdvance.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Advanceelab.cdacelabAdvance.entity.ReleaseNote1;
import com.Advanceelab.cdacelabAdvance.repository.ReleaseNote1Repo;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;

@Controller
public class ReleaseNotesController {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
    private ReleaseNote1Repo releaseNote1Repo;
	
	 @GetMapping("/releaseNote")
		public String ReleaseNote(Model model) 
	    {	
	    	List<ReleaseNote1> note=releaseNote1Repo.findAll();  	
	    	model.addAttribute("note", note);
			return "releaseNote";
		}
	    
	    
	    @PostMapping("/releaseNoteUpdate/{id}")
	    public String ReleaseNoteUpdate(@PathVariable("id") int id, Model model) 
	    {      
	        Optional<ReleaseNote1> releaseNoteOptional = releaseNote1Repo.findById(id);
	        if (releaseNoteOptional.isPresent()) {
	            ReleaseNote1 releaseNote = releaseNoteOptional.get();
	            model.addAttribute("releaseNote", releaseNote);
	            System.out.println(releaseNote);
	        } else {
	            // Handle the case where the entity with the provided ID is not found
	        }
	        return "releaseNoteUpdate"; 
	    }

	    
	    
	    @PostMapping("/releaseNoteUpdate1")
	    public String ReleaseNoteUpdate1(@ModelAttribute ReleaseNote1 releaseNote, @RequestParam("id") int id, @RequestParam("note1") String note1, @RequestParam(value = "pdfFile", required = false) 
	    MultipartFile pdfFile, BindingResult bindingResult, Model model, HttpSession session, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
	        // Validate CSRF token
	        CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);

	        if (!csrf.getToken().equals(csrfToken)) {
	            return "error.html"; // Invalid CSRF token, handle appropriately
	        }

	        // Validate HPP code
	        String[] params = new String[] { Integer.toString(id), note1, pdfFile.getOriginalFilename()};
	        
	        if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	            return "error.html"; // Invalid HPP code, handle appropriately
	        }

	        if (bindingResult.hasErrors()) {
	            return "releaseNote";
	        }

	        try {
	            // Update the note1 field with the provided value
	            releaseNote.setNote1(note1);
	            
	            // Check if a new PDF file has been uploaded
	            if (pdfFile != null && !pdfFile.isEmpty()) {
	                // Validate the file if needed
	                if (!isValidPDF(pdfFile)) {
	                    model.addAttribute("error", "Invalid PDF file format.");
	                    return "releaseNote";
	                }

	                // If a new PDF file is uploaded, update the PDF data
	                byte[] fileBytes = pdfFile.getBytes();
	                releaseNote.setPdfData(fileBytes);
	            } else {
	                // If no new PDF file is uploaded, retrieve the existing PDF data and set it back to the releaseNote object
	                ReleaseNote1 existingReleaseNote = releaseNote1Repo.findById(id).orElse(null);
	                if (existingReleaseNote != null) {
	                    releaseNote.setPdfData(existingReleaseNote.getPdfData());
	                }
	            }
	            
	            // Set the date and time
	            releaseNote.setDob(LocalDate.now());
	            releaseNote.setTime(LocalTime.now());
	            
	            // Save the releaseNote object
	            releaseNote1Repo.save(releaseNote);
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Handle the exception, maybe redirect to an error page
	            return "redirect:/releaseNote";
	        }

	        session.setAttribute("update", "Note Updated Successfully !!!");
	        return "redirect:/releaseNote";
	    }
	    
	    
	    private boolean isValidPDF(MultipartFile pdfFile) {
	        try {
	            // Check if the content type of the file is PDF
	            if (!pdfFile.getContentType().equalsIgnoreCase("application/pdf")) {
	                return false;
	            }
	            
	            // Alternatively, you can also check the file extension
	            String fileName = pdfFile.getOriginalFilename();
	            if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
	                return false;
	            }

	            // Optionally, you can also check the PDF file content for specific characteristics
	            // For example, you can use a library like Apache PDFBox to parse the PDF and check its structure
	            
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Handle the exception if any error occurs during validation
	            return false;
	        }
	    }


	    @PostMapping("/releaseNote1")
	    public String releaseNote1(@ModelAttribute ReleaseNote1 releaseNote, @RequestParam("_csrf") String csrfToken,@RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile, 
	    		BindingResult bindingResult, Model model, HttpSession session,HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
	        if (bindingResult.hasErrors()) {
	            return "releaseNote";
	        }

	        try {
	            // Check CSRF token validity
	            CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
	            if (!csrf.getToken().equals(csrfToken)) {
	                return "error.html"; // Return error page for invalid CSRF token
	            }

	            // Validate request parameters for HPP
	            String[] params = new String[] { releaseNote.getNote1(),pdfFile.getOriginalFilename() }; // Modify according to your request parameters

	            if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	                logger.info("HTTP Parameter pollution detected");
	                return "error.html"; // Return error page for HPP
	            }

	            // Save the note if all checks pass
	            if (pdfFile != null && !pdfFile.isEmpty()) {
	                byte[] fileBytes = pdfFile.getBytes();
	                releaseNote.setPdfData(fileBytes);
	            }
	            releaseNote.setDob(LocalDate.now());
	            releaseNote.setTime(LocalTime.now());
	            releaseNote1Repo.save(releaseNote);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "redirect:/releaseNote";
	        }

	        session.setAttribute("add", "Note Added Successfully !!!");
	        return "redirect:/releaseNote";
	    }


	    
	    @PostMapping("/releaseNote/{id}")
	    public String deleteReleaseNote(@PathVariable("id") int id, @RequestParam("_csrf") String csrfToken, HttpSession session,HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) {
	        // Check CSRF token validity
	        CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
	        if (!csrf.getToken().equals(csrfToken)) {
	            return "error.html"; // Return error page for invalid CSRF token
	        }

	        ReleaseNote1 releaseNote = releaseNote1Repo.findById(id).orElse(null);
	        if (releaseNote == null) {
	            return "redirect:/releaseNote";
	        }

	        try {
	            // Validate request parameters for HPP
	            String[] params = new String[] { String.valueOf(id) }; // Modify according to your request parameters
	           
	            if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
	                logger.info("HTTP Parameter pollution detected");
	                return "error.html"; // Return error page for HPP
	            }

	            releaseNote1Repo.delete(releaseNote);
	            session.setAttribute("del", "Note Deleted Successfully !!!");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "redirect:/releaseNote";
	        }

	        return "redirect:/releaseNote";
	    }

	    @PostMapping("/displayPDFNote/{id}")
		public void displayPDFNote(@PathVariable int id, HttpServletResponse response, @RequestParam("_csrf") String csrfToken, HttpServletRequest request,@RequestParam(value = "hppCode") String hppCode) throws IOException {
			
	    	CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
			if (csrf.getToken().equals(csrfToken)) {
				String a=String.valueOf(id);
				String[] params = new String[] { a };
				if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
					 response.sendRedirect("/error");
		        }
			}
			try {
				ReleaseNote1 releaseNote = releaseNote1Repo.findById(id).orElseThrow();
				byte[] pdffile = releaseNote.getPdfData();
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


}
