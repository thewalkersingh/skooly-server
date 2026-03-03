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
	Optional<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
	
	List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate from, LocalDate to);
	
	List<Attendance> findBySchoolClassIdAndDate(Long classId, LocalDate date);
	
	List<Attendance> findByStudentIdAndDateBetweenOrderByDateAsc(
			Long studentId, LocalDate from, LocalDate to
	                                                            );
	
	@Query("""
			    SELECT a FROM Attendance a
			    WHERE a.student.id = :studentId
			    AND MONTH(a.date) = :month
			    AND YEAR(a.date) = :year
			    ORDER BY a.date ASC
			""")
	List<Attendance> findByStudentAndMonthYear(
			@Param("studentId") Long studentId,
			@Param("month") int month,
			@Param("year") int year
	                                          );
	
	@Query("""
			    SELECT a FROM Attendance a
			    WHERE a.schoolClass.id = :classId
			    AND MONTH(a.date) = :month
			    AND YEAR(a.date) = :year
			""")
	List<Attendance> findByClassAndMonthYear(
			@Param("classId") Long classId,
			@Param("month") int month,
			@Param("year") int year
	                                        );
	
	@Query("""
			    SELECT COUNT(a) FROM Attendance a
			    WHERE a.student.id = :studentId
			    AND a.status = 'PRESENT'
			    AND a.date BETWEEN :from AND :to
			""")
	long countPresentDays(
			@Param("studentId") Long studentId,
			@Param("from") LocalDate from,
			@Param("to") LocalDate to
	                     );
	
	@Query("""
			    SELECT COUNT(a) FROM Attendance a
			    WHERE a.student.id = :studentId
			    AND a.date BETWEEN :from AND :to
			""")
	long countTotalDays(
			@Param("studentId") Long studentId,
			@Param("from") LocalDate from,
			@Param("to") LocalDate to
	                   );
	
	boolean existsByStudentIdAndDate(Long studentId, LocalDate date);
}