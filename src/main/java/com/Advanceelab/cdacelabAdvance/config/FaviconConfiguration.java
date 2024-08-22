package com.Advanceelab.cdacelabAdvance.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public class FaviconConfiguration {
	 @Bean
	    public SimpleUrlHandlerMapping customFaviconHandlerMapping() {
	        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
	        mapping.setOrder(Integer.MIN_VALUE);
	        mapping.setUrlMap(Collections.singletonMap(
	                "favicon.ico", faviconRequestHandler()));
	        return mapping;
	    }

	    @Bean
	    protected ResourceHttpRequestHandler faviconRequestHandler() {
	        ResourceHttpRequestHandler requestHandler
	                = new ResourceHttpRequestHandler();
	        requestHandler.setLocations(Collections.singletonList(new ClassPathResource("/")));
	        return requestHandler;
	    }
}
