package com.skooly.data;

import com.github.javafaker.Faker;
import com.skooly.dto.request.SchoolRequest;
import com.skooly.enums.SchoolStatus;
import com.skooly.repository.SchoolRepository;
import com.skooly.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SchoolData {
	
	private final SchoolService schoolService;
	private final SchoolRepository schoolRepository;
	
	//	@Bean
//	@Order(2)
	public CommandLineRunner seedSchools() {
		
		return args -> {
			Faker faker = new Faker();
			
			List<String> schoolNames =
				List.of("Green Valley School", "Sunrise Public School", "Delhi International School");
			
			for (int i = 0; i < schoolNames.size(); i++) {
				
				// SCH001, SCH002, SCH003 — deterministic, never duplicates
				String schoolCode = String.format("SCH%03d", i + 1);
				
				// Idempotent — skip if already exists
				if (schoolRepository.existsBySchoolCode(schoolCode)) {
					continue;
				}
				
				SchoolRequest request = SchoolRequest
					                        .builder()
					                        .schoolName(schoolNames.get(i))
					                        .schoolCode(schoolCode)
					                        .address(faker.address().fullAddress())
					                        .phone(faker.phoneNumber().subscriberNumber(10))
					                        .email(schoolCode.toLowerCase() + "@skooly.com")
					                        .logoUrl(faker.internet().avatar())
					                        .schoolStatus(SchoolStatus.ACTIVE)
					                        .build();
				
				schoolService.createSchool(request);
			}
		};
	}
	
}