package com.horizon.skoolyserver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkoolyServerApplication {
	static void main(String[] args) {
		SpringApplication.run(SkoolyServerApplication.class, args);
		startServer();
	}
	
	private static void startServer() {
		System.out.println("+----------------------------------+");
		System.out.println("|     (•_•)   App Started...!!     |");
		System.out.println("+----------------------------------+");
	}
}
