package com.horizon.skoolyserver.service;
import com.horizon.skoolyserver.dto.school.SchoolClassDTO;
import com.horizon.skoolyserver.model.SchoolClass;
import com.horizon.skoolyserver.repository.SchoolClassRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SchoolClassService {
	private final SchoolClassRepository schoolClassRepository;
	
	// DTO -> Entity
	private SchoolClass mapToEntity(SchoolClassDTO dto) {
		SchoolClass schoolClass = new SchoolClass();
		schoolClass.setId(dto.getId());
		schoolClass.setGrade(dto.getGrade());
		schoolClass.setSection(dto.getSection());
		return schoolClass;
	}
	
	// Entity -> DTO
	private SchoolClassDTO mapToDTO(SchoolClass schoolClass) {
		SchoolClassDTO dto = new SchoolClassDTO();
		dto.setId(schoolClass.getId());
		dto.setGrade(schoolClass.getGrade());
		dto.setSection(schoolClass.getSection());
		return dto;
	}
	
	public SchoolClassDTO create(@RequestBody SchoolClassDTO schoolClass) {
		return mapToDTO(schoolClassRepository.save(mapToEntity(schoolClass)));
	}
	
	public SchoolClassDTO findById(Long id) {
		return schoolClassRepository.findById(id)
		                            .map(this::mapToDTO)
		                            .orElseThrow(() -> new RuntimeException("Student not found"));
	}
	
	public List<SchoolClassDTO> findAll() {
		return schoolClassRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	public List<SchoolClassDTO> findBySection(String section) {
		return schoolClassRepository.findBySection(section).stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	public List<SchoolClassDTO> findByGrade(String grade) {
		return schoolClassRepository.findByGrade(grade).stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	public void deleteById(Long id) {
		schoolClassRepository.deleteById(id);
	}
}
