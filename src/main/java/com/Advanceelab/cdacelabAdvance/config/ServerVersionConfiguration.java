package com.Advanceelab.cdacelabAdvance.config;


import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerVersionConfiguration {

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("server", ""); // Set an empty string to hide server version  
            connector.setProperty("serverHeader", ""); // Set an empty string to hide server name
        });
        return factory;
    }
}

