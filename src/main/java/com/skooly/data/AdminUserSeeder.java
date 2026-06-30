package com.skooly.data;

import com.skooly.entity.User;
import com.skooly.entity.UserIdentity;
import com.skooly.enums.Gender;
import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import com.skooly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminUserSeeder {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Bean
	@Order(1)
	public CommandLineRunner seedAdminUser() {
		
		return args -> {
			
			String adminPhone = "8888888888";
			String adminEmail = "the.walkersingh@gmail.com";
			
			// Idempotent — skip if already exists
			if (userRepository.existsByIdentityPhone(adminPhone)) {
				log.info("Admin user already exists — skipping");
				return;
			}
			
			UserIdentity identity =
				UserIdentity.builder()
				            .firstName("Super")
				            .lastName("Admin")
				            .phone(adminPhone)
				            .email(adminEmail)
				            .gender(Gender.MALE)
				            .build();
			
			User admin = User.builder().identity(identity)
			                 .password(passwordEncoder.encode("Admin@1234"))  // change after first login
			                 .role(UserRole.ADMIN).roleEntityId(null)     // ADMIN is not linked to any entity
			                 .status(UserStatus.ACTIVE).firstLogin(false)      // ADMIN doesn't need OTP flow
			                 .build();
			
			userRepository.save(admin);
			log.info("Admin user seeded — email: {} | password: Admin@1234", adminEmail);
		};
	}
	
}