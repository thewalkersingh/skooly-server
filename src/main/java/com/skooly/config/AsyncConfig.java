package com.skooly.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync   // enables @Async on NotificationServiceImpl methods
public class AsyncConfig {
	// Spring uses a default thread pool for @Async methods
	// Replace with custom executor later if you need fine-grained control
}