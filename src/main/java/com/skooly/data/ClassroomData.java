package com.skooly.data;

import com.github.javafaker.Faker;
import com.skooly.dto.request.ClassroomRequest;
import com.skooly.entity.School;
import com.skooly.enums.ClassroomStatus;
import com.skooly.repository.ClassroomRepository;
import com.skooly.repository.SchoolRepository;
import com.skooly.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ClassroomData {
	
	private final ClassroomService classroomService;
	private final SchoolRepository schoolRepository;
	private final ClassroomRepository classroomRepository;
	
	//	@Bean
//	@Order(3)
	public CommandLineRunner seedClassrooms() {
		
		return args -> {
			Faker faker = new Faker();
			
			List<School> schools = schoolRepository.findAll();
			if (schools.isEmpty()) throw new RuntimeException("No schools found — seed schools first");
			
			String[] grades = {"Grade 1", "Grade 2", "Grade 3", "Grade 4",
				"Grade 5", "Grade 6", "Grade 7", "Grade 8"};
			
			for (School school : schools) {
				for (String grade : grades) {
					
					// Idempotent — skip if already exists for this school
					if (classroomRepository
						    .existsBySchoolIdAndClassroomCode(
							    school.getId(),
							    school.getSchoolCode() + "-" + grade.replace(" ", ""))) {
						continue;
					}
					
					ClassroomRequest request =
						ClassroomRequest.builder()
						                .classroomName(grade)
						                // unique per school: "GVS001-Grade1"
						                .classroomCode(
							                school
								                .getSchoolCode() + "-" + grade.replace(" ", ""))
						                .classroomStatus(ClassroomStatus.ACTIVE)
						                .build();
					
					classroomService.createClassroom(school.getId(), request);
				}
			}
		};
	}
	
}