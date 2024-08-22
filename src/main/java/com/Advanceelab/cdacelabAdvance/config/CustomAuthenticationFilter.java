package com.Advanceelab.cdacelabAdvance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import com.Advanceelab.cdacelabAdvance.dto.MyUser;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.security.EncodingUtility2;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private UserRepository userRepo;
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
	    try {
	        // Attempt to authenticate user
	        UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
	        setDetails(request, authRequest);
	        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
	       
	        // If authentication is successful, return it
	        return authentication;
	    } catch (AuthenticationException e) {
	        // Delete the cookie upon authentication failure
	        deleteCookie(request, response);
	        throw e; // Rethrow the exception to propagate it further
	    }
	}
		
	private void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals("myCustomSessionCookieName")) {
	                cookie.setMaxAge(0);
	                response.addCookie(cookie);
	                System.out.println("deleting cookie ");
	                break;
	            }
	        }
	    }
	}
	
	

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
       
    	String username = obtainUsername(request).toLowerCase();
        String password = obtainPassword(request);
	
    	User user2=userRepo.findByUsername(username);
    	if (user2!=null && !user2.isApproved()) {
    	    LocalDate currentDate = LocalDate.now();
    	    LocalDate userLoginDate = user2.getLoginTime();// Assuming getLoginTime() returns LocalDateTime

    	    // Compare the login date with the current date
    	    if (userLoginDate.isBefore(currentDate)) {
    	        user2.setApproved(true);
    	        user2.setLoginAttempt(0);
    	        user2.setLoginTime(null);
    	        userRepo.save(user2);
    	    }  
    	}

        
        if(!isApproved(username)) {
        	
        	throw new AuthenticationException("User is not approved") {
			private static final long serialVersionUID = 1L;};
        }
        int seed=5;
        password=EncodingUtility2.decodeWithSeedNEW(password, seed);
       
        // Obtain and validate captcha
        String captcha = obtainCaptcha(request);
        if (!validateCaptcha(captcha, request.getSession())) {
        	
        	
        	
            throw new BadCredentialsException("Invalid captcha");
        }

        // Create a new session (invalidate the existing one if it exists)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        if(!username.equals("cg_4232@cybergyan.in"))
        {
        	
        	User user=userRepo.findByUsername(username);
        	
        	if(!user.isApproved())
        	{
        		user.getLoginAttempt();
        	}
        	
        	
        	int a=user.getLoginAttempt()+1;
        	
        	user.setLoginAttempt(a);
        	 LocalDate currentDate = LocalDate.now();
        	 user.setLoginTime(currentDate);
        	
        	if(user.getLoginAttempt()>=5)
        	{
        		user.setApproved(false);
        		user.setLoginTime(currentDate);
        	}
            
       
//            user.setLoginTime(now);
        	userRepo.save(user);
        	
        }
        	
        request.getSession(true); // Create a new session

        MyUser.setMypassword(password);
        MyUser.setUsername(username);

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    private boolean isApproved(String username) {
		User user=userRepo.findByUsername(username);
		if (user==null) {
			return false;
		}
		return user.isApproved();
	}

	private String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter("captcha");
    }

    private boolean validateCaptcha(String captcha, HttpSession session) {
        String challenge = (String) session.getAttribute("captchaChallenge");
        return captcha != null && challenge != null && captcha.equals(challenge);
    }
}
