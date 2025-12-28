package com.horizon.skoolyserver.service;
import com.horizon.skoolyserver.dto.attendance.AttendanceDTO;
import com.horizon.skoolyserver.exception.ResourceNotFoundException;
import com.horizon.skoolyserver.model.Attendance;
import com.horizon.skoolyserver.model.Student;
import com.horizon.skoolyserver.repository.AttendanceRepository;
import com.horizon.skoolyserver.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AttendanceService {
	private final AttendanceRepository attendanceRepository;
	private final StudentRepository studentRepository;
	
	public AttendanceDTO createAttendance(AttendanceDTO dto) {
		Attendance saved = attendanceRepository.save(mapToEntity(dto));
		return mapToDTO(saved);
	}
	
	public List<AttendanceDTO> getAllAttendances() {
		return attendanceRepository.findAll().stream().map(this::mapToDTO).toList();
	}
	
	public AttendanceDTO getAttendanceById(Long id) {
		return attendanceRepository.findById(id)
				       .map(this::mapToDTO)
				       .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));
	}
	
	public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {
		return attendanceRepository.findByDate(date).stream().map(this::mapToDTO).toList();
	}
	
	// Current it is returning List of DTO
	public List<AttendanceDTO> getAttendanceByStudentId(Long studentId) {
		return attendanceRepository.findByStudentId(studentId).stream().map(this::mapToDTO).toList();
	}
	
	public AttendanceDTO updateAttendance(Long id, AttendanceDTO dto) {
		Attendance attendance = attendanceRepository.findById(id)
				                        .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));
		attendance.setDate(dto.getDate());
		attendance.setStatus(dto.getStatus());
		if(dto.getStudentId() != null){
			Student student = studentRepository.findById(dto.getStudentId())
					                  .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
			attendance.setStudent(student);
		}
		return mapToDTO(attendanceRepository.save(attendance));
	}
	
	public void deleteAttendanceById(Long id) {
		if (!attendanceRepository.existsById(id)) {
			throw new ResourceNotFoundException("Attendance not found");
		}
		attendanceRepository.deleteById(id);
	}
	
	
	//Entity -> DTO
	private AttendanceDTO mapToDTO(Attendance attendance) {
		AttendanceDTO dto = new AttendanceDTO();
		dto.setId(attendance.getId());
		dto.setDate(attendance.getDate());
		dto.setStatus(attendance.getStatus());
		if(attendance.getStudent() != null){
			dto.setStudentId(attendance.getStudent().getId());
		}
		return dto;
	}
	
	// DTO -> Entity
	private Attendance mapToEntity(AttendanceDTO dto) {
		Attendance attendance = new Attendance();
		attendance.setId(dto.getId());
		attendance.setDate(dto.getDate());
		attendance.setStatus(dto.getStatus());
		if(dto.getStudentId() != null){
			Student student = studentRepository.findById(dto.getStudentId())
					                  .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
			attendance.setStudent(student);
		}
		return attendance;
	}
}
