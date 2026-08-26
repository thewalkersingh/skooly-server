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
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminUserSeeder {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	//	@Bean
//	@Order(1)
	public CommandLineRunner seedAdminUser() {
		
		return args -> {
			
			String superAdminPhone = "8888888888";
			String superAdminEmail = "the.walkersingh@gmail.com";
			String adminPhone = "9999999999";
			String adminEmail = "anysignup47@gmail.com";
			
			// Idempotent — skip if already exists
			if (userRepository.existsByIdentityPhone(superAdminPhone) ||
				    userRepository.existsByIdentityEmail(adminPhone)) {
				log.info("Admin user already exists — skipping");
				return;
			}
			
			UserIdentity superIdentity =
				UserIdentity.builder()
				            .firstName("Super")
				            .lastName("Admin")
				            .phone(superAdminPhone)
				            .email(superAdminEmail)
				            .gender(Gender.MALE)
				            .build();
			
			User superadmin = User.builder().identity(superIdentity)
			                      .password(passwordEncoder.encode("admin@1234"))  // change after first login
			                      .userRole(UserRole.SUPER_ADMIN)
			                      .roleEntityId(null)     // ADMIN is not linked to any entity
			                      .userStatus(UserStatus.ACTIVE).firstLogin(false)      // ADMIN doesn't need OTP flow
			                      .build();
			userRepository.save(superadmin);
			log.info("Super Admin user seeded — email: {} | password: admin@1234", superAdminEmail);
			
			UserIdentity adminIdentity =
				UserIdentity.builder()
				            .firstName("Admin")
				            .lastName("Normal")
				            .phone(adminPhone)
				            .email(adminEmail)
				            .gender(Gender.MALE)
				            .build();
			
			User admin = User.builder().identity(adminIdentity)
			                 .password(passwordEncoder.encode("admin@1234"))  // change after first login
			                 .userRole(UserRole.ADMIN)
			                 .roleEntityId(null)     // ADMIN is not linked to any entity
			                 .userStatus(UserStatus.ACTIVE).firstLogin(false)      // ADMIN doesn't need OTP flow
			                 .build();
			userRepository.save(admin);
			log.info("Admin user seeded — email: {} | password: admin@1234", adminEmail);
		};
	}
	
}