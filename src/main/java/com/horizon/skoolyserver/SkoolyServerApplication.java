package com.horizon.skoolyserver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkoolyServerApplication {
	static void main(String[] args) {
		SpringApplication.run(SkoolyServerApplication.class, args);
		startServer();
	}
	
	static void startServer() {
		System.out.println("+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+\n");
		System.out.println("(_   _)( )                  ( )       ( )   ( )            ( )\n" +
		                   "  | |  | |__     _ _   ___  | |/')    `\\`\\_/'/'_    _   _  | |\n" +
		                   "  | |  |  _ `\\ /'_` )/' _ `\\| , <       `\\ /'/'_`\\ ( ) ( ) | |\n" +
		                   "  | |  | | | |( (_| || ( ) || |\\`\\       | |( (_) )| (_) | | |\n" +
		                   "  (_)  (_) (_)`\\__,_)(_) (_)(_) (_)      (_)`\\___/'`\\___/' (_)\n" +
		                   "\t\t\t\t\t                   \n");
		System.out.print("+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+\n");
	}
}
