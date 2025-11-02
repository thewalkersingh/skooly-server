package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.dto.AttendanceDTO;
import com.horizon.skoolyserver.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@AllArgsConstructor
public class AttendanceController {
	private final AttendanceService attendanceService;
	
	@PostMapping
	public ResponseEntity<AttendanceDTO> createAttendance(@Valid @RequestBody AttendanceDTO dto) {
		AttendanceDTO created = attendanceService.createAttendance(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	@GetMapping
	public ResponseEntity<List<AttendanceDTO>> getAllAttendance() {
		return ResponseEntity.ok(attendanceService.getAllAttendances());
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<AttendanceDTO> getAttendanceById(@PathVariable Long id) {
		return ResponseEntity.ok(attendanceService.getAttendanceById(id));
	}
	
	@GetMapping("/date/{date}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(@PathVariable LocalDate date) {
		return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
	}
	
	@GetMapping("/student/{id}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentId(@PathVariable Long id) {
		return ResponseEntity.ok(attendanceService.getAttendanceByStudentId(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AttendanceDTO> updateAttendance(@PathVariable Long id,
			@Valid @RequestBody AttendanceDTO dto) {
		return ResponseEntity.ok(attendanceService.updateAttendance(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
		attendanceService.deleteAttendanceById(id);
		return ResponseEntity.noContent().build();
	}
}

