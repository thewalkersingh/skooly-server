package com.skooly.data;

import com.github.javafaker.Faker;
import com.skooly.dto.request.AddressRequest;
import com.skooly.dto.request.TeacherRequest;
import com.skooly.dto.request.UserIdentityRequest;
import com.skooly.entity.School;
import com.skooly.entity.Section;
import com.skooly.entity.Subject;
import com.skooly.entity.Teacher;
import com.skooly.enums.Gender;
import com.skooly.enums.TeacherStatus;
import com.skooly.repository.SchoolRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.repository.SubjectRepository;
import com.skooly.repository.TeacherRepository;
import com.skooly.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TeacherData {
	
	private final TeacherService teacherService;
	private final SchoolRepository schoolRepository;
	private final SubjectRepository subjectRepository;
	private final SectionRepository sectionRepository;
	private final TeacherRepository teacherRepository;
	
	@Bean
	@Order(7)
	public CommandLineRunner seedTeachers() {
		return args -> {
			Faker faker = new Faker();
			
			List<School> schools = schoolRepository.findAll();
			if (schools.isEmpty()) throw new RuntimeException("No schools found — seed schools first");
			
			List<Subject> allSubjects = subjectRepository.findAll();
			if (allSubjects.isEmpty()) throw new RuntimeException("No subjects found — seed subjects first");
			
			// Track index for deterministic phone/email — avoids duplicates on re-run
			int teacherIndex = 0;
			
			for (School school : schools) {
				for (int i = 1; i <= 5; i++) {
					teacherIndex++;
					
					// Deterministic phone/email — unique and idempotent
					String phone = String.format("900000%04d", teacherIndex);
					String email = String.format("teacher%04d@skooly.com", teacherIndex);
					
					// Idempotent — skip if already exists
					if (teacherRepository.existsByIdentityPhone(phone)) continue;
					
					// Assign 2-3 subjects round-robin per teacher
					List<Long> subjectIds = allSubjects.stream()
						                        .skip((teacherIndex - 1) % allSubjects.size())
						                        .limit(3)
						                        .map(Subject::getId)
						                        .toList();
					
					TeacherRequest request = TeacherRequest.builder()
						                         .qualification(faker.educator().campus())
						                         .experience(faker.number().numberBetween(1, 20))
						                         .photoUrl(faker.internet().avatar())
						                         .dob(LocalDate.of(
							                         faker.number().numberBetween(1970, 1995),
							                         faker.number().numberBetween(1, 12),
							                         faker.number().numberBetween(1, 28)))
						                         .joiningDate(LocalDate.now())
						                         .address(AddressRequest.builder()
							                                  .houseNumber(faker.address().buildingNumber())
							                                  .streetName(faker.address().streetName())
							                                  .zipCode(faker.address().zipCode())
							                                  .city(faker.address().city())
							                                  .state(faker.address().state())
							                                  .build())
						                         .status(faker.options().option(TeacherStatus.ACTIVE))
						                         // never seed DELETED/INACTIVE
						                         .subjectIds(subjectIds)
						                         .identity(UserIdentityRequest.builder()
							                                   .firstName(faker.name().firstName())
							                                   .lastName(faker.name().lastName())
							                                   .phone(phone)
							                                   .email(email)
							                                   .gender(faker.options().option(Gender.MALE, Gender.FEMALE))
							                                   .build())
						                         .build();
					
					teacherService.createTeacher(school.getId(), request);
				}
			}
		};
	}
	
	@Bean
	@Order(8)
	public CommandLineRunner seedSectionTeachers() {
		return args -> {
			List<School> schools = schoolRepository.findAll();
			
			for (School school : schools) {
				List<Teacher> teachers = teacherRepository.findBySchoolId(school.getId());
				List<Section> unassigned = sectionRepository
					                           .findUnassignedSectionsBySchoolId(school.getId());
				
				// Assign teachers to unassigned sections round-robin
				for (int i = 0; i < unassigned.size(); i++) {
					if (teachers.isEmpty()) break;
					Section section = unassigned.get(i);
					Teacher teacher = teachers.get(i % teachers.size());
					section.setTeacher(teacher);
					sectionRepository.save(section);
				}
			}
		};
	}
	
}