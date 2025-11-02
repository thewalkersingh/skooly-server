package com.horizon.skoolyserver.service;
import com.horizon.skoolyserver.dto.StudentDTO;
import com.horizon.skoolyserver.exception.ResourceNotFoundException;
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
		student.setId(dto.getId()); // ensure new entity
		Student saved = studentRepository.save(student);
		return mapToDTO(saved);
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
				       .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
	}
	
	public StudentDTO updateStudent(Long id, StudentDTO dto) {
		Student student = studentRepository.findById(id)
				                  .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
		
		student.setAdmissionNumber(dto.getAdmissionNumber());
		student.setFirstName(dto.getFirstName());
		student.setLastName(dto.getLastName());
		student.setEmail(dto.getEmail());
		student.setPhone(dto.getPhone());
		// TODO: update address, city, etc. if included in DTO
		
		Student updated = studentRepository.save(student);
		return mapToDTO(updated);
	}
	
	public void deleteStudent(Long id) {
		if(!studentRepository.existsById(id)){
			throw new ResourceNotFoundException("Student not found with id " + id);
		}
		studentRepository.deleteById(id);
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
		dto.setAge(student.getAge());
		dto.setAddress(student.getAddress());
		dto.setCity(student.getCity());
		dto.setState(student.getState());
		dto.setCountry(student.getCountry());
		dto.setZip(student.getZip());
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
//		student.setId(dto.getId());
		student.setAdmissionNumber(dto.getAdmissionNumber());
		student.setFirstName(dto.getFirstName());
		student.setLastName(dto.getLastName());
		student.setEmail(dto.getEmail());
		student.setPhone(dto.getPhone());
		student.setAge(dto.getAge());
		student.setAddress(dto.getAddress());
		student.setCity(dto.getCity());
		student.setState(dto.getState());
		student.setCountry(dto.getCountry());
		student.setZip(dto.getZip());
		// TODO: fetch SchoolClass and Parent by ID if needed
		return student;
	}
}