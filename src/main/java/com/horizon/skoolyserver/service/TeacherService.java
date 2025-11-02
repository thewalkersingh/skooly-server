package com.horizon.skoolyserver.service;
import com.horizon.skoolyserver.dto.TeacherDTO;
import com.horizon.skoolyserver.exception.ResourceNotFoundException;
import com.horizon.skoolyserver.model.Teacher;
import com.horizon.skoolyserver.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
	private final TeacherRepository teacherRepository;
	
	public TeacherDTO createTeacher(TeacherDTO dto) {
		Teacher teacher = mapToEntity(dto);
		teacher.setId(null);// ensure new entity
		Teacher saved = teacherRepository.save(teacher);
		return mapToDTO(saved);
	}
	
	public List<TeacherDTO> getAllTeachers() {
		return teacherRepository.findAll()
				       .stream()
				       .map(this::mapToDTO)
				       .collect(Collectors.toList());
	}
	
	public TeacherDTO getTeacherById(Long id) {
		return teacherRepository.findById(id)
				       .map(this::mapToDTO)
				       .orElseThrow(() -> new ResourceNotFoundException("No teacher with id " + id + " found"));
	}
	
	public TeacherDTO getTeacherByEmployeeCode(String empCode) {
		Teacher teacher = teacherRepository.findByEmployeeCode(empCode);
		return mapToDTO(teacher);
	}
	
	public TeacherDTO updateTeacher(Long id, TeacherDTO dto) {
		Teacher teacher = teacherRepository.findById(id)
				                  .orElseThrow(
						                  () -> new ResourceNotFoundException("No teacher with id " + id + " found"));
		teacher.setName(dto.getName());
		teacher.setEmployeeCode(dto.getEmployeeCode());
		teacher.setEmail(dto.getEmail());
		teacher.setPhone(dto.getPhone());
		teacher.setSubjects(dto.getSubjects());
		
		Teacher updated = teacherRepository.save(teacher);
		return mapToDTO(updated);
	}
	
	public void deleteTeacher(Long id) {
		if(!teacherRepository.existsById(id)){
			throw new ResourceNotFoundException("No teacher with id " + id + " found");
		}
		teacherRepository.deleteById(id);
	}
	
	// Convert Entity -> DTO
	private TeacherDTO mapToDTO(Teacher teacher) {
		TeacherDTO dto = new TeacherDTO();
		dto.setId(teacher.getId());
		dto.setEmployeeCode(teacher.getEmployeeCode());
		dto.setName(teacher.getName());
		dto.setEmail(teacher.getEmail());
		dto.setPhone(teacher.getPhone());
		dto.setSubjects(teacher.getSubjects());
		return dto;
	}
	
	// Convert DTO -> Entity
	private Teacher mapToEntity(TeacherDTO dto) {
		Teacher teacher = new Teacher();
		teacher.setId(dto.getId());
		teacher.setEmployeeCode(dto.getEmployeeCode());
		teacher.setName(dto.getName());
		teacher.setEmail(dto.getEmail());
		teacher.setPhone(dto.getPhone());
		teacher.setSubjects(dto.getSubjects());
		return teacher;
	}
}
