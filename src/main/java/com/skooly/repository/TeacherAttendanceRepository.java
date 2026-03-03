package com.skooly.repository;
import com.skooly.model.TeacherAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherAttendanceRepository extends JpaRepository<TeacherAttendance, Long> {
	Optional<TeacherAttendance> findByTeacherIdAndDate(Long teacherId, LocalDate date);
	
	boolean existsByTeacherIdAndDate(Long teacherId, LocalDate date);
	
	@Query("""
			    SELECT a FROM TeacherAttendance a
			    WHERE a.teacher.id = :teacherId
			    AND MONTH(a.date) = :month
			    AND YEAR(a.date) = :year
			    ORDER BY a.date ASC
			""")
	List<TeacherAttendance> findByTeacherAndMonthYear(
			@Param("teacherId") Long teacherId,
			@Param("month") int month,
			@Param("year") int year
	                                                 );
}