//package com.skooly.data;
//
//import com.github.javafaker.Faker;
//import com.skooly.dto.request.AddressRequest;
//import com.skooly.dto.request.ParentRequest;
//import com.skooly.dto.request.UserIdentityRequest;
//import com.skooly.entity.Student;
//import com.skooly.enums.Gender;
//import com.skooly.repository.StudentRepository;
//import com.skooly.service.ParentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class ParentData {
//
//	private final ParentService parentService;
//	private final StudentRepository studentRepository;
//
//	//	@Bean
/// /	@Order(7)
//	public CommandLineRunner seedParents() {
//		return args -> {
//			Faker faker = new Faker();
//			Long randomId = (long) faker.number().numberBetween(1, 10);
//			// ✅ fetch existing students to assign parents
//			List<Long> studentIds = studentRepository.findById((long) faker.number().numberBetween(1, 20))
//				                         .map(Student::getId)
//				                         .stream()
//				                         .limit(2)// assign first 2 students for demo
//				                         .toList();
//
//			for (int i = 1; i <= 10; i++) {
//				ParentRequest request = ParentRequest.builder()
//					                         .occupation(faker.company().profession())
//					                         .relation(faker.options().option("Father", "Mother", "Guardian"))
//					                         .address(AddressRequest.builder()
//						                                   .houseNumber(faker.address().buildingNumber())
//						                                   .streetName(faker.address().streetName())
//						                                   .zipCode(faker.address().zipCode())
//						                                   .city(faker.address().city())
//						                                   .state(faker.address().state())
//						                                   .build())
//					                         .identity(UserIdentityRequest.builder()
//						                                    .firstName(faker.name().firstName())
//						                                    .lastName(faker.name().lastName())
//						                                    .phone(faker.phoneNumber().cellPhone())
//						                                    .email(faker.internet().emailAddress())
//						                                    .gender(faker.options().option(Gender.class))
//						                                    .build())
//					                         .studentIds(List.of(1L, 2L)) // dummy student IDs
//					                         .build();
//
//				parentService.createParent(request);
//			}
//		};
//	}
//
//}