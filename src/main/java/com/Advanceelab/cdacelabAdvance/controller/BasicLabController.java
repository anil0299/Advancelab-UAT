package com.Advanceelab.cdacelabAdvance.controller;

import java.io.IOException;
//import java.util.Base64;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

//import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
//import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.service.BasicLabService;
//import com.Advanceelab.cdacelabAdvance.service.GuacamoleService;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Controller
public class BasicLabController {

    @Autowired
    private BasicLabService basicLabService;
    
//    @Autowired
//    private GuacamoleService guacamoleService;
    
//    @Autowired
//    private StudentRepository studentRepo;

    @GetMapping("/basicDescription")
    public ModelAndView basiclab(ModelAndView mav) throws IOException, InterruptedException, ExecutionException {
        String labemail = getUsername();
        String userpass = basicLabService.generateUserPass(labemail);

        boolean vmExist = basicLabService.checkAndPrepareVms(labemail, userpass, mav);
//        if(vmExist) {
//        	
//        	//Guacamole Code for user creation
//        	String authToken = guacamoleService.getAdminToken().get();
//        	StudentDtls studentDtls = studentRepo.findByLabemail(labemail);
//    		String fullName = studentDtls.getFirstName()+" "+studentDtls.getLastName();
//    		String username = null;
//    		String hcipassword = null;
//    		if (authToken !=null) {
//    			username = labemail.substring(0, labemail.indexOf('@'));
//    			boolean userFound = guacamoleService.checkDetailsOfUser(username, authToken).get();
//    			if(!userFound) {
//    				hcipassword = basicLabService.generateHciPassword(studentDtls.getFirstName(),studentDtls.getDob());
//    				String response = guacamoleService.createUser(username, hcipassword, authToken, fullName).get();
//    				if(response != null) {
//    					System.out.println(response);
//    				} else {
//    					System.out.println("User not created");
//    				}
//    			} else {
//    				hcipassword = basicLabService.generateHciPassword(studentDtls.getFirstName(),studentDtls.getDob());
//    			}
//    		}
    		
//    		//Create connection in guacamole & assign the connection to the user
//			String connectionIdentifier = null;
//			if(mav.getModel().get("vmnameshowwin")!=null && mav.getModel().get("vmnameshowlinux") !=null) {
//				
//				String vmNameWindows = mav.getModel().get("vmnameshowwin").toString();
//				String ipAddressWindows = mav.getModel().get("ipaddresswin").toString();
//
//				String response = guacamoleService.createConnection(vmNameWindows, ipAddressWindows, authToken).get();
//				System.out.println(response);
//				
//				if(response.equals("RDP connection created in guacamole.") || response.equals("RDP connection already exists.")) {
//					connectionIdentifier = guacamoleService.getConnectionIdentifier(vmNameWindows, authToken).get();
//					if(connectionIdentifier != null) {
//						//System.out.println(connectionIdentifier);
//						boolean assigned = guacamoleService.assignConnection(username, authToken, connectionIdentifier).get();
//						if(assigned) {
//							System.out.println("Connection assigned to the user");
//						} else {
//							System.out.println("Connection not assigned");
//						}
//					} else {
//						System.out.println("Machine Identifier not found");
//					}
//				}
//				else {
//					System.err.println("Connection creation failed");
//				}
//				
//				String vmNameLinux = mav.getModel().get("vmnameshowlinux").toString();
//				String ipAddressLinux = mav.getModel().get("ipaddresslinux").toString();
//				
//				response = guacamoleService.createConnection(vmNameLinux, ipAddressLinux, authToken).get();
//				System.out.println(response);
//
//				if(response.equals("RDP connection created in guacamole.") || response.equals("RDP connection already exists.")) {
//					connectionIdentifier = guacamoleService.getConnectionIdentifier(vmNameLinux, authToken).get();
//					if(connectionIdentifier != null) {
//						//System.out.println(connectionIdentifier);
//						boolean assigned = guacamoleService.assignConnection(username, authToken, connectionIdentifier).get();
//						if(assigned) {
//							System.out.println("Connection assigned to the user");
//						} else {
//							System.out.println("Connection not assigned");
//						}
//					} else {
//						System.out.println("Machine Identifier not found");
//					}
//				}
//				else {
//					System.err.println("Connection creation failed");
//				}
//
//				//Generating user token
//	        	String userToken = guacamoleService.getUserToken(username, hcipassword).get();
//	    		userToken = Base64.getEncoder().encodeToString(userToken.getBytes());
//	    		mav.addObject("token",userToken);
//			}
//        } else {
//        	String username = labemail.substring(0, labemail.indexOf('@'));
//        	String authToken = guacamoleService.getAdminToken().get();
//        	String linuxVM = guacamoleService.getConnectionIdentifier(username+"-linux", authToken).get();
//        	String windowVM = guacamoleService.getConnectionIdentifier(username+"-window", authToken).get();
//        	if(linuxVM != null) {
//        		guacamoleService.deleteConnection(linuxVM, authToken);
//        	}
//        	if(windowVM != null) {
//        		guacamoleService.deleteConnection(windowVM, authToken);
//        	}
//        }
        
        mav.addObject("vmexist", vmExist);
        mav.setViewName("BasicDescription");
        return mav;
    }

    @GetMapping("/createbasiclab")
    public ModelAndView createbasiclab(ModelAndView mav) throws IOException, InterruptedException, ExecutionException {
    	String labemail = getUsername();
        String userpass = basicLabService.generateUserPass(labemail);
        basicLabService.createAndStartVms(labemail, userpass, mav);
        Thread.sleep(5000);
        return new ModelAndView("redirect:/basicDescription");
    }

    @PostMapping("/deletevmbasiclab")
    public ModelAndView deletevm(@RequestParam("submissionPdf") MultipartFile submissionPdf,
                                 ModelAndView mav, @RequestParam("_csrf") String csrfToken, 
                                 HttpServletRequest request, HttpSession session) throws IOException, InterruptedException {
        try {
            CsrfToken csrf = new HttpSessionCsrfTokenRepository().loadToken(request);
            String labemail = getUsername();
            String userpass = basicLabService.generateUserPass(labemail);
            byte[] pdffile1 = submissionPdf.getBytes();
            
            if (!basicLabService.isPdfContentSafe(submissionPdf)) {
                session.setAttribute("message", "pdf is not allowed.");
                return new ModelAndView("redirect:/createbasiclab");
            }

            if (csrf.getToken().equals(csrfToken)) {
            	basicLabService.saveSubmission(labemail, pdffile1, submissionPdf.getOriginalFilename());
                basicLabService.deleteVms(labemail, userpass);
            }
			
//            String username = labemail.substring(0, labemail.indexOf('@'));
//            
//			String vmNameWindows = username + "-window";
//			String vmNameLinux = username + "-linux";
//			  
//			String authToken = guacamoleService.getAdminToken().get();
//			  
//			String connectionIdentifier = guacamoleService.getConnectionIdentifier(vmNameWindows, authToken).get();
//			guacamoleService.deleteConnection(connectionIdentifier, authToken).get(); 
//			  
//			connectionIdentifier = guacamoleService.getConnectionIdentifier(vmNameLinux, authToken).get(); 
//			guacamoleService.deleteConnection(connectionIdentifier, authToken).get();
			
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ModelAndView("error");
        }
        Thread.sleep(5000);
        return new ModelAndView("redirect:/basicDescription");
    }

    private String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
