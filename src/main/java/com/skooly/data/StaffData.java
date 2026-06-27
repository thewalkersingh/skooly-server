package com.skooly.data;

import com.github.javafaker.Faker;
import com.skooly.dto.request.AddressRequest;
import com.skooly.dto.request.StaffRequest;
import com.skooly.dto.request.UserIdentityRequest;
import com.skooly.entity.School;
import com.skooly.enums.Department;
import com.skooly.enums.Gender;
import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import com.skooly.repository.SchoolRepository;
import com.skooly.repository.StaffRepository;
import com.skooly.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StaffData {
	
	private final StaffService staffService;
	private final StaffRepository staffRepository;
	private final SchoolRepository schoolRepository;
	
	@Bean
	@Order(11)
	public CommandLineRunner seedStaff() {
		return args -> {
			Faker faker = new Faker();
			
			List<School> schools = schoolRepository.findAll();
			if (schools.isEmpty()) throw new RuntimeException("No schools found — seed schools first");
			
			// One staff member per role per school — realistic and bounded
			record RoleConfig(StaffRole role, Department department) {
			
			}
			
			List<RoleConfig> roleConfigs = List.of(
				new RoleConfig(StaffRole.ADMIN, Department.ADMINISTRATION),
				new RoleConfig(StaffRole.ACCOUNTANT, Department.FINANCE),
				new RoleConfig(StaffRole.LIBRARIAN, Department.LIBRARY),
				new RoleConfig(StaffRole.LAB_ASSISTANT, Department.SCIENCE),
				new RoleConfig(StaffRole.DRIVER, Department.TRANSPORT),
				new RoleConfig(StaffRole.SECURITY_GUARD, Department.SECURITY)
			);
			
			int staffIndex = 0;
			
			for (School school : schools) {
				for (RoleConfig config : roleConfigs) {
					staffIndex++;
					
					// Deterministic — unique and idempotent
					String phone = String.format("600000%04d", staffIndex);
					String email = String.format("staff%04d@skooly.com", staffIndex);
					
					// Idempotent — skip if already exists
					if (staffRepository.existsByIdentityPhone(phone)) continue;
					
					StaffRequest request = StaffRequest.builder()
					                                   .staffRole(config.role())
					                                   .department(config.department())
					                                   .status(StaffStatus.ACTIVE)
					                                   .qualification(faker.educator().campus())
					                                   .experience(faker.number().numberBetween(1, 20))
					                                   .joiningDate(LocalDate.of(
						                                   faker.number().numberBetween(2010, 2023),
						                                   faker.number().numberBetween(1, 12),
						                                   faker.number().numberBetween(1, 28)))
					                                   .dob(LocalDate.of(
						                                   faker.number().numberBetween(1970, 1995),
						                                   faker.number().numberBetween(1, 12),
						                                   faker.number().numberBetween(1, 28)))
					                                   .photoUrl(faker.internet().avatar())
					                                   .address(AddressRequest.builder()
					                                                          .houseNumber(faker.address().buildingNumber())
					                                                          .streetName(faker.address().streetName())
					                                                          .zipCode(faker.address().zipCode())
					                                                          .city(faker.address().city())
					                                                          .state(faker.address().state())
					                                                          .build())
					                                   .identity(UserIdentityRequest.builder()
					                                                                .firstName(faker.name().firstName())
					                                                                .lastName(faker.name().lastName())
					                                                                .phone(phone)
					                                                                .email(email)
					                                                                .gender(faker.options()
					                                                                             .option(Gender.MALE,
						                                                                             Gender.FEMALE))
					                                                                .build())
					                                   .build();
					
					staffService.createStaff(school.getId(), request);
				}
			}
		};
	}
	
}