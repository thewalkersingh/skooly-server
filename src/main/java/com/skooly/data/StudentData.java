//package com.skooly.data;
//
//import com.github.javafaker.Faker;
//import com.skooly.dto.request.AddressRequest;
//import com.skooly.dto.request.StudentRequest;
//import com.skooly.dto.request.UserIdentityRequest;
//import com.skooly.entity.Parent;
//import com.skooly.entity.Section;
//import com.skooly.enums.Gender;
//import com.skooly.enums.StudentStatus;
//import com.skooly.repository.ParentRepository;
//import com.skooly.repository.SectionRepository;
//import com.skooly.service.StudentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//
//import java.time.LocalDate;
//
//@Configuration
//@RequiredArgsConstructor
//public class StudentData {
//
//	private final StudentService studentService; // use service layer for mapping + saving
//	private final SectionRepository sectionRepository;
//	private final ParentRepository parentRepository;
//
//	@Bean
//	@Order(6)
//	public CommandLineRunner seedStudents() {
//		return args -> {
//			Faker faker = new Faker();
//			Long randomId = (long) faker.number().numberBetween(1, 10);
//			Long sectionId = sectionRepository.findById((long) faker.number().numberBetween(1, 10))
//			                                  .map(Section::getId)
//			                                  .orElseThrow(() -> new RuntimeException("No section found"));
//			// fetch a parent to assign students
//			Long parentId = parentRepository.findById((long) faker.number().numberBetween(1, 10))
//			                                .map(Parent::getId)
//			                                .orElseThrow(() -> new RuntimeException("No Parent found"));
//			for (int i = 1; i <= 10; i++) {
//				StudentRequest request = StudentRequest.builder()
//				                                       .dob(LocalDate.of(2005, 5, 15))
//				                                       .admissionDate(LocalDate.now())
//				                                       .photoUrl(faker.internet().avatar())
//				                                       .studentStatus(faker.options().option(StudentStatus.class))
//				                                       .sectionId(sectionId)
//				                                       .parentId(parentId)
//				                                       .address(AddressRequest.builder()
//				                                                              .houseNumber(faker.address()
//				                                                              .buildingNumber())
//				                                                              .streetName(faker.address().streetName())
//				                                                              .zipCode(faker.address().zipCode())
//				                                                              .city(faker.address().city())
//				                                                              .state(faker.address().state())
//				                                                              .build())
//				                                       .identity(UserIdentityRequest.builder()
//				                                                                    .firstName(faker.name().firstName())
//				                                                                    .lastName(faker.name().lastName())
//				                                                                    .phone(faker.phoneNumber().cellPhone())
//				                                                                    .email(faker.internet().emailAddress())
//				                                                                    .gender(
//					                                                                    faker.options().option(Gender
//					                                                                    .class))
//				                                                                    .build())
//				                                       .build();
//
//				studentService.createStudent(randomId, request); // persist via service
//			}
//		};
//	}
//
//}