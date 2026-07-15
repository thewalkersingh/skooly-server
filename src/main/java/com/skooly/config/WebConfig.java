package com.skooly.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				
				registry.addMapping("/api/**")
				        .allowedOrigins(
					        "http://localhost:5173",
					        "http://localhost:8080/swagger-ui",
					        "https://skooly.vercel.app",
					        "https://skooly-1buzk9nt9-diwakar-singhs-projects.vercel.app")
				        .allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}
	
}