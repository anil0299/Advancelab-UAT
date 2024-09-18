package com.Advanceelab.cdacelabAdvance.listiner;


import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import com.Advanceelab.cdacelabAdvance.entity.StudentTrackTime;
import com.Advanceelab.cdacelabAdvance.repository.StudentTrackTimeRepository;
import com.Advanceelab.cdacelabAdvance.service.BasicLabService;


@Component
public class CustomSessionListener implements HttpSessionListener{
	
	@Autowired
	private StudentTrackTimeRepository studentTrackTimeRepo;
	
	@Autowired
    private BasicLabService basicLabService;

	@Override
    public void sessionDestroyed(HttpSessionEvent event) {
		 SecurityContext securityContext = (SecurityContext) event.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
         if (securityContext != null && securityContext.getAuthentication() != null) {
         	boolean hasUserRole = securityContext.getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER"));
         	if(hasUserRole)
         	{
         		String username = securityContext.getAuthentication().getName();
                 //System.out.println("Session expired for user: " + username);
                 StudentTrackTime studentTrackTime = studentTrackTimeRepo.findByUsername(username);

                 if (studentTrackTime != null) {
 	           	    ZonedDateTime lastLoginTime = studentTrackTime.getLastloginTime();
 	           	    ZonedDateTime currentTime = ZonedDateTime.now();
 	           	    Duration duration = Duration.between(lastLoginTime, currentTime);
 	
 	           	    studentTrackTime.setLogoutTime(currentTime);
 	           	    if(studentTrackTime.getTimeSpentInPortal() != null)
 	           	    {
	 	           	    studentTrackTime.setTimeSpentInPortal(studentTrackTime.getTimeSpentInPortal().plus(duration));
 	           	    }
 	           	    else
 	           	    {
 	           	    	studentTrackTime.setTimeSpentInPortal(duration);
 	           	    }
 	           	    studentTrackTimeRepo.save(studentTrackTime);
 	           	    String userpass = basicLabService.generateUserPass(username);
 	           	    try {
						basicLabService.deleteVms(username, userpass);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
 	           	}
         	}
         }
    }
}
