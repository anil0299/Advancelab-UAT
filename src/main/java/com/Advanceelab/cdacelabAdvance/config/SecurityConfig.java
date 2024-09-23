package com.Advanceelab.cdacelabAdvance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.Advanceelab.cdacelabAdvance.listiner.CustomSessionListener;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	private SessionExpiredHandler sessionExpiredHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		http.addFilterBefore(new HttpMethodFilter(), ChannelProcessingFilter.class); //check for external url
		
		http.authorizeRequests()
		
				
				.antMatchers(HttpMethod.PUT).denyAll()
				.antMatchers(HttpMethod.DELETE).denyAll()
				.antMatchers(HttpMethod.PATCH).denyAll()
				.antMatchers(HttpMethod.HEAD).denyAll()
				.antMatchers(HttpMethod.TRACE).denyAll()
				
				.antMatchers("/css/**","/fontawesome/**", "/welcome/**", "/registration/**", "/TeacherRegistration/**", "/createUser/**",
						"/registerTeacher/**", "/images/**", "/js/**", "/favicon.ico", "/colleges/**",
						"/checkUniqueEmail/**", "/checkUniqueEmailteacher/**", "/checkApprovalStatus/**","/refreshCaptcha","/","/displayPDFNote/**","/resetPassword/**","/checkMobileNumber","/checkMobileNumber2").permitAll()
				
				.antMatchers("/home", "/ConfigVPN/**", "/filter/**", "/AccessLAB/**", "/dashboard","/createAdvanceLab/**","/launchConsole","/Re-Submit/**").hasAuthority("USER")
				
				.antMatchers("/adminpage", "/pdfs/**", "/folders/**", "/dashboard", "/LabDetails", "/filter",
                        "/AssignBatch", "/extend-validity/", "/TeacherApproval", "/basiclab", "/pdfbasic", "/ExerciseCompletion",
                        "/RegisteredStudents", "/Completionstatus1", "/Statewise", "/Categorywise", "/StateandCategory",
                        "/TeacherApproval/approve-teachertDtls/**", "/deleteuser/**", "/deletecourse/**", "/updateSavebatchex/**", "/quizCompletion/**", "/submissionData", "/submissionData/**", "/pdf", "/pdf/**",
                        "/registeredStudentsData", "/registeredStudentsData/**","/basicCompletionStatusData","/basicCompletionStatusData/**","/advanceCompletionStatusData","/advanceCompletionStatusData/**",
                        "labCompletionStatusData","/labCompletionStatusData/**","/fetchQuizTitles","/fetchQuizTitles/**", "/quizCompletionData", "/quizCompletionData/**","/labDetailsData","/labDetailsData/**",
                        "/pendingStudentData", "/pendingStudentData/**", "/approvedStudentData", "/approvedStudentData/**", "/rejectedStudentData", "/rejectedStudentData/**")
                .hasAuthority("ADMIN")
				
				.antMatchers("/AddExercise", "/StudentApproval", "/teacherApproval", "/exercisedetails", "/AdminHome",
						"/vmdetails", "/addvm", "/assignExercise", "/Batchexercise", "/CenterDetails", "/AddCourse",
						"/AddCenter","/displayPDF/**","/savecenter","/displayPDF5/**","/displayPDF1/**","/AddExerciseContent","/updateExerciseContent","/exerciseContent","/AddExerciseImage","/StudentTrackTime")
				.hasAuthority("ADMIN")
				
			
				.antMatchers("/deleteexer/**", "/showupdateexer","/folders", "/AssignExercise", "/savebatchex", "updatebatchex",
						"/delexerbatch/**", "/deletevm/**", "/showupdatevm", "/vmdetails", "/savevm","/deletecenter/**","/updatecenter/**","/releaseNote","/releaseNoteUpdate1/**","/releaseNoteUpdate/**")
				.hasAuthority("ADMIN")
				
				.antMatchers("/AccessBasicCourse","/assignClass","/createClass", "/CreateClassByTeacher", "/AssignExerciseByTeacher",
						"/classexercisedetails", "/CompletionStatusTeacherView", "/checkUniqueclassName",
						"/CompletionStatusTeacherlms", "/getStudentlist", "/search/api/getSearchResult/**","/AssignExerbyClass","/TeacherHome")
				.hasAuthority("TEACHER")
				
				.anyRequest().authenticated().and()
				.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).formLogin()
				.loginPage("/login").successHandler(loginSuccessHandler()).failureUrl("/login?error=true").permitAll()
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
				.logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID").and()
				.sessionManagement().sessionFixation().migrateSession()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).invalidSessionUrl("/login?expired=true")
				.maximumSessions(1).expiredUrl("/login?expired=true").and()
				
			
				 .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
     
		 .and()
	        .csrf()
	        .csrfTokenRepository(csrfTokenRepository())
	        .and();
		
		   // remove header
		
		        http.headers().disable();
		        http.headers()
		            .frameOptions().disable()  // Example: Disable X-Frame-Options
		            .httpStrictTransportSecurity().disable(); // Example: Disable Strict-Transport-Security
		
	}
	
	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
	    return new SessionFixationProtectionStrategy();
	}
	
	
	@Bean
	public CsrfTokenRepository csrfTokenRepository() {
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	    repository.setHeaderName("X-XSRF-TOKEN"); // You can customize the header name if needed
	    return repository;
	}


    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(failureHandler());
        return filter;
    }

	@Bean
	public SimpleUrlAuthenticationFailureHandler failureHandler() {
		return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
	}

	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return (request, response, authentication) -> 
		{
			response.sendRedirect("/");
		};
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(16);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
//	@Bean
//    public ServletListenerRegistrationBean<CustomSessionListener> sessionListener() {
//        return new ServletListenerRegistrationBean<>(new CustomSessionListener());
//    }
}
