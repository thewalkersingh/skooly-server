package com.skooly.data;

import com.github.javafaker.Faker;
import com.skooly.entity.Classroom;
import com.skooly.entity.Section;
import com.skooly.repository.ClassroomRepository;
import com.skooly.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SectionData {
	
	private final SectionRepository sectionRepository;
	private final ClassroomRepository classroomRepository;
	
	//	@Bean
//	@Order(4)
	public CommandLineRunner seedSections() {
		return args -> {
			Faker faker = new Faker();
			
			// Fetch all classrooms — we'll create A, B, C for each one deterministically
			List<Classroom> classrooms = classroomRepository.findAll();
			if (classrooms.isEmpty()) throw new RuntimeException("No classrooms found — seed classrooms first");
			
			String[] sectionNames = {"A", "B", "C"};
//			int teacherIndex = 0;
			
			for (Classroom classroom : classrooms) {
				for (String name : sectionNames) {
					
					// Avoid duplicate section names per classroom on re-run
					if (sectionRepository.existsByClassroomIdAndSectionName(classroom.getId(), name)) {
						continue;
					}
					
					Section section = Section.builder()
						                  .sectionName(name)
						                  .classroom(classroom)
						                  .teacher(null)       // assigned later when Teacher seeder runs
						                  .capacity(faker.number().numberBetween(30, 60))
						                  .build();
					
					sectionRepository.save(section);
				}
			}
		};
	}
	
}