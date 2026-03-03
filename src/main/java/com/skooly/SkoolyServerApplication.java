package com.skooly;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync  // ← enables async logging
@EnableScheduling
public class SkoolyServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkoolyServerApplication.class, args);
		start();
	}
	
	private static void start() {
		System.out.println("==================================================================");
		System.out.println("||                                                               ||");
		System.out.println("||    ███████╗██████╗ ██████╗ ██╗███╗   ██╗ ██████╗  ██████╗     ||");
		System.out.println("||    ██╔════╝██╔══██╗██╔══██╗██║████╗  ██║██╔═══██╗██╔═══██╗    ||");
		System.out.println("||    █████╗  ██████╔╝██████╔╝██║██╔██╗ ██║██║   ██║██║   ██║    ||");
		System.out.println("||    ██╔══╝  ██╔═══╝ ██╔═══╝ ██║██║╚██╗██║██║   ██║██║   ██║    ||");
		System.out.println("||    ███████╗██║     ██║     ██║██║ ╚████║╚██████╔╝╚██████╔╝    ||");
		System.out.println("||    ╚══════╝╚═╝     ╚═╝     ╚═╝╚═╝  ╚═══╝ ╚═════╝  ╚═════╝     ||");
		System.out.println("||                                                               ||");
		System.out.println("||                 Spring Boot App Started                       ||");
		System.out.println("||                                                               ||");
		System.out.println("==================================================================");
	}
}
