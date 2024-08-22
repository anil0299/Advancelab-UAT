//package com.Advanceelab.cdacelabAdvance.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import com.Advanceelab.cdacelabAdvance.entity.StudentTrackTime;
//import com.Advanceelab.cdacelabAdvance.repository.StudentTrackTimeRepository;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.Duration;
//import java.time.ZonedDateTime;
//
//
//@Component
//public class SessionExpiredHandler implements LogoutSuccessHandler {
//	
//	@Autowired
//	private StudentTrackTimeRepository studentTrackTimeRepo;
//	
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
//                                Authentication authentication) throws IOException, ServletException {
//    	
//    	 if (authentication != null && authentication.isAuthenticated()) {
//    		 
//    		 boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("USER"));
//             if(hasUserRole)
//             {
//            	 String username = authentication.getName();
//                 StudentTrackTime studentTrackTime = studentTrackTimeRepo.findByUsername(username);
//
//                 if (studentTrackTime != null) {
//            	    ZonedDateTime lastLoginTime = studentTrackTime.getLastloginTime();
//            	    ZonedDateTime currentTime = ZonedDateTime.now();
//            	    Duration duration = Duration.between(lastLoginTime, currentTime);
//
//            	    studentTrackTime.setLogoutTime(currentTime);
//            	    if(studentTrackTime.getTimeSpentInPortal() != null)
//            	    	studentTrackTime.setTimeSpentInPortal(studentTrackTime.getTimeSpentInPortal().plus(duration));
//            	    else
//            	    	studentTrackTime.setTimeSpentInPortal(duration);
//            	    System.out.println("onLogoutSuccess save");
//            	    studentTrackTimeRepo.save(studentTrackTime);
//            	}
//             }
//         }
//    	 request.getSession().setAttribute("LOGOUT_FLAG", true);
//        response.sendRedirect(request.getContextPath() + "/login"); // Redirect to the login page
//    }
//}
