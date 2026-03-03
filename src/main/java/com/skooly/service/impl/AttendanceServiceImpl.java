package com.skooly.service.impl;
import com.skooly.dto.request.BulkAttendanceRequest;
import com.skooly.dto.request.MarkAttendanceRequest;
import com.skooly.dto.request.MarkTeacherAttendanceRequest;
import com.skooly.dto.response.AttendanceResponse;
import com.skooly.dto.response.AttendanceSummaryResponse;
import com.skooly.dto.response.TeacherAttendanceResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.AttendanceMapper;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
	private final AttendanceRepository attendanceRepository;
	private final TeacherAttendanceRepository teacherAttendanceRepository;
	private final StudentRepository studentRepository;
	private final SchoolClassRepository classRepository;
	private final TeacherRepository teacherRepository;
	private final UserRepository userRepository;
	private final AttendanceMapper attendanceMapper;
	
	// ── Student Attendance ───────────────────────────────────────────────────
	
	@Override
	public AttendanceResponse markAttendance(MarkAttendanceRequest request, Long markedByUserId) {
		if(attendanceRepository.existsByStudentIdAndDate(request.getStudentId(), request.getDate())){
			throw new BadRequestException("Attendance already marked for this student on "+request.getDate());
		}
		Attendance attendance = buildAttendance(request, markedByUserId);
		return attendanceMapper.toResponse(attendanceRepository.save(attendance));
	}
	
	@Override
	public List<AttendanceResponse> markBulkAttendance(BulkAttendanceRequest request, Long markedByUserId) {
		SchoolClass schoolClass = findClassById(request.getClassId());
		User markedBy = userRepository.findById(markedByUserId)
				                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		return request.getEntries().stream().map(entry -> {
			if(attendanceRepository.existsByStudentIdAndDate(entry.getStudentId(), request.getDate())){
				throw new BadRequestException("Attendance already marked for student ID: "+entry.getStudentId());
			}
			Student student = findStudentById(entry.getStudentId());
			Attendance attendance = Attendance.builder()
					                        .student(student)
					                        .schoolClass(schoolClass)
					                        .date(request.getDate())
					                        .status(entry.getStatus())
					                        .markedBy(markedBy)
					                        .remarks(entry.getRemarks())
					                        .build();
			return attendanceMapper.toResponse(attendanceRepository.save(attendance));
		}).toList();
	}
	
	@Override
	public AttendanceResponse updateAttendance(Long id, MarkAttendanceRequest request) {
		Attendance attendance = findAttendanceById(id);
		attendance.setStatus(request.getStatus());
		if(request.getRemarks() != null)
			attendance.setRemarks(request.getRemarks());
		return attendanceMapper.toResponse(attendanceRepository.save(attendance));
	}
	
	@Override
	public void deleteAttendance(Long id) {
		if(!attendanceRepository.existsById(id)){
			throw new ResourceNotFoundException("Attendance record not found with id: "+id);
		}
		attendanceRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public AttendanceResponse getAttendanceById(Long id) {
		return attendanceMapper.toResponse(findAttendanceById(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AttendanceResponse> getClassAttendanceByDate(Long classId, LocalDate date) {
		return attendanceRepository.findBySchoolClassIdAndDate(classId, date)
				       .stream().map(attendanceMapper::toResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AttendanceResponse> getStudentMonthlyAttendance(Long studentId, int month, int year) {
		return attendanceRepository.findByStudentAndMonthYear(studentId, month, year)
				       .stream().map(attendanceMapper::toResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public AttendanceSummaryResponse getStudentAttendanceSummary(Long studentId, LocalDate from, LocalDate to) {
		Student student = findStudentById(studentId);
		long total = attendanceRepository.countTotalDays(studentId, from, to);
		long present = attendanceRepository.countPresentDays(studentId, from, to);
		long absent = total-present;
		double percentage = total > 0 ? Math.round((present * 100.0 / total) * 100.0) / 100.0 : 0.0;
		
		return AttendanceSummaryResponse.builder()
				       .studentId(studentId)
				       .studentName(student.getFirstName()+" "+student.getLastName())
				       .totalDays(total)
				       .presentDays(present)
				       .absentDays(absent)
				       .attendancePercentage(percentage)
				       .build();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AttendanceSummaryResponse> getLowAttendanceStudents(
			Long classId, int month, int year, double threshold) {
		
		LocalDate from = LocalDate.of(year, month, 1);
		LocalDate to = from.withDayOfMonth(from.lengthOfMonth());
		
		return attendanceRepository.findByClassAndMonthYear(classId, month, year)
				       .stream()
				       .map(a -> a.getStudent())
				       .distinct()
				       .map(student -> {
					       long total = attendanceRepository.countTotalDays(student.getId(), from, to);
					       long present = attendanceRepository.countPresentDays(student.getId(), from, to);
					       double pct = total > 0 ? Math.round((present * 100.0 / total) * 100.0) / 100.0 : 0.0;
					       return AttendanceSummaryResponse.builder()
							              .studentId(student.getId())
							              .studentName(student.getFirstName()+" "+student.getLastName())
							              .totalDays(total)
							              .presentDays(present)
							              .absentDays(total-present)
							              .attendancePercentage(pct)
							              .build();
				       })
				       .filter(s -> s.getAttendancePercentage() < threshold)
				       .toList();
	}
	
	// ── Teacher Attendance ───────────────────────────────────────────────────
	
	@Override
	public TeacherAttendanceResponse markTeacherAttendance(MarkTeacherAttendanceRequest request) {
		if(teacherAttendanceRepository.existsByTeacherIdAndDate(request.getTeacherId(), request.getDate())){
			throw new BadRequestException("Attendance already marked for this teacher on "+request.getDate());
		}
		Teacher teacher = findTeacherById(request.getTeacherId());
		TeacherAttendance attendance = TeacherAttendance.builder()
				                               .teacher(teacher)
				                               .date(request.getDate())
				                               .status(request.getStatus())
				                               .remarks(request.getRemarks())
				                               .build();
		return attendanceMapper.toTeacherResponse(teacherAttendanceRepository.save(attendance));
	}
	
	@Override
	public List<TeacherAttendanceResponse> markBulkTeacherAttendance(
			List<MarkTeacherAttendanceRequest> requests) {
		return requests.stream().map(this::markTeacherAttendance).toList();
	}
	
	@Override
	public TeacherAttendanceResponse updateTeacherAttendance(Long id, MarkTeacherAttendanceRequest request) {
		TeacherAttendance attendance = teacherAttendanceRepository.findById(id)
				                               .orElseThrow(() -> new ResourceNotFoundException(
						                               "Teacher attendance not found with id: "+id));
		attendance.setStatus(request.getStatus());
		if(request.getRemarks() != null)
			attendance.setRemarks(request.getRemarks());
		return attendanceMapper.toTeacherResponse(teacherAttendanceRepository.save(attendance));
	}
	
	@Override
	public void deleteTeacherAttendance(Long id) {
		if(!teacherAttendanceRepository.existsById(id)){
			throw new ResourceNotFoundException("Teacher attendance not found with id: "+id);
		}
		teacherAttendanceRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<TeacherAttendanceResponse> getTeacherMonthlyAttendance(Long teacherId, int month, int year) {
		return teacherAttendanceRepository.findByTeacherAndMonthYear(teacherId, month, year)
				       .stream().map(attendanceMapper::toTeacherResponse).toList();
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Attendance buildAttendance(MarkAttendanceRequest request, Long markedByUserId) {
		Student student = findStudentById(request.getStudentId());
		SchoolClass clazz = findClassById(request.getClassId());
		User markedBy = userRepository.findById(markedByUserId)
				                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return Attendance.builder()
				       .student(student)
				       .schoolClass(clazz)
				       .date(request.getDate())
				       .status(request.getStatus())
				       .markedBy(markedBy)
				       .remarks(request.getRemarks())
				       .build();
	}
	
	private Attendance findAttendanceById(Long id) {
		return attendanceRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: "+id));
	}
	
	private Student findStudentById(Long id) {
		return studentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: "+id));
	}
	
	private SchoolClass findClassById(Long id) {
		return classRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: "+id));
	}
	
	private Teacher findTeacherById(Long id) {
		return teacherRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: "+id));
	}
}