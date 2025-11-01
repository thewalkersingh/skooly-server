package com.horizon.skoolyserver.service;
import com.horizon.skoolyserver.dto.StudentDTO;
import com.horizon.skoolyserver.model.Student;
import com.horizon.skoolyserver.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
	private final StudentRepository studentRepository;
	
	public StudentDTO createStudent(StudentDTO dto) {
		Student student = mapToEntity(dto);
		Student saved = studentRepository.save(student);
		return mapToDTO(saved);
	}
	
	// Convert Entity -> DTO
	private StudentDTO mapToDTO(Student student) {
		StudentDTO dto = new StudentDTO();
		dto.setId(student.getId());
		dto.setAdmissionNumber(student.getAdmissionNumber());
		dto.setFirstName(student.getFirstName());
		dto.setLastName(student.getLastName());
		dto.setEmail(student.getEmail());
		dto.setPhone(student.getPhone());
		if(student.getSchoolClass() != null){
			dto.setClassId(student.getSchoolClass().getId());
		}
		if(student.getParent() != null){
			dto.setParentId(student.getParent().getId());
		}
		return dto;
	}
	
	// Convert DTO -> Entity
	private Student mapToEntity(StudentDTO dto) {
		Student student = new Student();
		student.setId(dto.getId());
		student.setAdmissionNumber(dto.getAdmissionNumber());
		student.setFirstName(dto.getFirstName());
		student.setLastName(dto.getLastName());
		student.setEmail(dto.getEmail());
		student.setPhone(dto.getPhone());
		// TODO: fetch SchoolClass and Parent by ID if needed
		return student;
	}
	
	public List<StudentDTO> getAllStudents() {
		return studentRepository.findAll()
		                        .stream()
		                        .map(this::mapToDTO)
		                        .collect(Collectors.toList());
	}
	
	public StudentDTO getStudentById(Long id) {
		return studentRepository.findById(id)
		                        .map(this::mapToDTO)
		                        .orElseThrow(() -> new RuntimeException("Student not found"));
	}
	
	public StudentDTO updateStudent(Long id, StudentDTO dto) {
		Student student = studentRepository.findById(id)
		                                   .orElseThrow(() -> new RuntimeException("Student not found"));
		student.setFirstName(dto.getFirstName());
		student.setLastName(dto.getLastName());
		student.setEmail(dto.getEmail());
		student.setPhone(dto.getPhone());
		Student updated = studentRepository.save(student);
		return mapToDTO(updated);
	}
	
	public void deleteStudent(Long id) {
		studentRepository.deleteById(id);
	}
}