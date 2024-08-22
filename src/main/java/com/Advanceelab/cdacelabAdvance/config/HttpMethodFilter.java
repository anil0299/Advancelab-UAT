package com.Advanceelab.cdacelabAdvance.config;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
@WebFilter("/*")
@Order(1)
public class HttpMethodFilter implements Filter {

	/*
	 * @Override public void doFilter(ServletRequest request, ServletResponse
	 * response, FilterChain chain) throws IOException, ServletException {
	 * HttpServletRequest httpRequest = (HttpServletRequest) request; String method
	 * = httpRequest.getMethod(); String requestURI = httpRequest.getRequestURI();
	 * 
	 * // Block PUT and DELETE requests to specific endpoints if
	 * (("PUT".equals(method) || "DELETE".equals(method)) &&
	 * (requestURI.contains("/your-put-endpoint") ||
	 * requestURI.contains("/your-delete-endpoint"))) { response.getWriter().
	 * write("PUT and DELETE requests are not allowed for this endpoint.");
	 * response.setContentType("text/plain"); ((HttpServletResponse)
	 * response).setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); } else {
	 * chain.doFilter(request, response); } }
	 */
	
	
	 @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	            throws IOException, ServletException {
	        HttpServletRequest httpRequest = (HttpServletRequest) request;
	        String method = httpRequest.getMethod();
	        

	        // Block PUT and DELETE requests to specific endpoints
	        if (("PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method) || "HEAD".equals(method) || "TRACE".equals(method))) {
	            response.getWriter().write("requests are not allowed for this endpoint.");
	            response.setContentType("text/plain");
	            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	        } else {
	            chain.doFilter(request, response);
	        }
	    }
	
	
}
