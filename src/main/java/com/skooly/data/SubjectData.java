package com.skooly.data;

import com.skooly.dto.request.SubjectRequest;
import com.skooly.entity.Section;
import com.skooly.entity.Subject;
import com.skooly.enums.SubjectStatus;
import com.skooly.repository.SectionRepository;
import com.skooly.repository.SubjectRepository;
import com.skooly.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SubjectData {
	
	private final SubjectService subjectService;
	private final SubjectRepository subjectRepository;
	private final SectionRepository sectionRepository;
	
	@Bean
	@Order(5)
	public CommandLineRunner seedSubjects() {
		return args -> {
			
			// code, name, status
			List<String[]> subjects = List.of(
				// Core — Primary
				new String[]{"MATH", "Mathematics", "ACTIVE"},
				new String[]{"ENG", "English", "ACTIVE"},
				new String[]{"HIN", "Hindi", "ACTIVE"},
				new String[]{"EVS", "Environmental Science", "ACTIVE"},
				new String[]{"PE", "Physical Education", "ACTIVE"},
				// Core — Middle (extras)
				new String[]{"SCI", "Science", "ACTIVE"},
				new String[]{"SOC", "Social Studies", "ACTIVE"},
				new String[]{"CS", "Computer Science", "ACTIVE"},
				// Core — Secondary (extras)
				new String[]{"PHY", "Physics", "ACTIVE"},
				new String[]{"CHEM", "Chemistry", "ACTIVE"},
				new String[]{"BIO", "Biology", "ACTIVE"},
				new String[]{"HIST", "History", "ACTIVE"},
				new String[]{"GEO", "Geography", "ACTIVE"},
				// Electives
				new String[]{"ART", "Art & Craft", "ELECTIVE"},
				new String[]{"MUSIC", "Music", "ELECTIVE"},
				new String[]{"DANCE", "Dance", "ELECTIVE"});
			
			for (String[] s : subjects) {
				if (subjectRepository.existsBySubjectCode(s[0])) continue;
				
				subjectService.createSubject(SubjectRequest
					                             .builder()
					                             .subjectCode(s[0])
					                             .subjectName(s[1])
					                             .status(SubjectStatus.valueOf(s[2]))
					                             .build());
			}
		};
	}
	
	//	@Bean
//	@Order(5)
	public CommandLineRunner seedSectionSubjectsData() {
		return args -> {
			
			// Core subject codes per grade group
			List<String> primaryCodes = List.of("MATH", "ENG", "HIN", "EVS", "PE");
			List<String> middleCodes = List.of("MATH", "ENG", "HIN", "SCI", "SOC", "CS", "PE");
			List<String> secondaryCodes = List.of("MATH", "ENG", "HIN", "PHY", "CHEM", "BIO", "HIST", "GEO", "CS");
			
			// Elective per section name
			Map<String, String> sectionElective = Map.of(
				"A", "ART",
				"B", "MUSIC",
				"C", "DANCE"
			);
			
			// Load all subjects into a map for O(1) lookup by code
			Map<String, Subject> subjectMap = subjectRepository.findAll()
				                                  .stream()
				                                  .collect(Collectors.toMap(Subject::getSubjectCode, s -> s));
			
			List<Section> sections = sectionRepository.findAllWithSubjectsAndClassroom();
			if (sections.isEmpty()) throw new RuntimeException("No sections found");
			
			for (Section section : sections) {
				
				// Idempotent
				if (!section.getSubjects().isEmpty()) continue;
				
				// Extract grade number from classroomName e.g. "Grade 7" → 7
				String classroomName = section.getClassroom().getClassroomName();
				int grade = Integer.parseInt(classroomName.split(" ")[1]);
				
				// Pick core subjects based on grade
				List<String> coreCodes = grade <= 3 ? primaryCodes
					                         : grade <= 7 ? middleCodes
					                           : secondaryCodes;
				
				// Collect core subjects
				List<Subject> assigned = new ArrayList<>(
					coreCodes.stream()
						.map(subjectMap::get)
						.filter(Objects::nonNull)
						.toList()
				);
				
				// Add elective based on section name (A/B/C)
				String electiveCode = sectionElective.get(section.getSectionName());
				if (electiveCode != null && subjectMap.containsKey(electiveCode)) {
					assigned.add(subjectMap.get(electiveCode));
				}
				
				section.getSubjects().addAll(assigned);
				sectionRepository.save(section);
			}
		};
	}
	
}