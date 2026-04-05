package com.skooly.repository;
import com.skooly.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	List<Attendance> findBySchoolIdAndDate(Long schoolId, LocalDate date);
	
	List<Attendance> findBySchoolIdAndSchoolClassIdAndDate(Long schoolId, Long classId, LocalDate date);
	
	List<Attendance> findBySchoolIdAndStudentId(Long schoolId, Long studentId);
	
	List<Attendance> findBySchoolIdAndSchoolClassIdAndDateBetween(
			Long schoolId, Long classId, LocalDate from, LocalDate to);
	
	Optional<Attendance> findBySchoolIdAndStudentIdAndDate(Long schoolId, Long studentId, LocalDate date);
	
	long countBySchoolIdAndDateAndStatus(Long schoolId, LocalDate date, Attendance.Status status);
	
	long countBySchoolIdAndDate(Long schoolId, LocalDate date);
	
	@Query("SELECT COUNT(DISTINCT a.student.id) FROM Attendance a " +
	       "WHERE a.school.id = :schoolId AND a.date = :date")
	long countDistinctStudentsBySchoolIdAndDate(@Param("schoolId") Long schoolId, @Param("date") LocalDate date);
	
}