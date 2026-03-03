package com.skooly.repository;
import com.skooly.model.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
	List<Result> findByExamId(Long examId);
	
	List<Result> findByStudentId(Long studentId);
	
	Optional<Result> findByExamIdAndStudentId(Long examId, Long studentId);
	
	boolean existsByExamIdAndStudentId(Long examId, Long studentId);
	
	@Query("""
			    SELECT r FROM Result r
			    WHERE r.exam.id = :examId
			    ORDER BY r.marksObtained DESC
			""")
	List<Result> findByExamIdOrderByMarksDesc(@Param("examId") Long examId);
	
	@Query("""
			    SELECT AVG(r.marksObtained) FROM Result r WHERE r.exam.id = :examId
			""")
	Double getAverageMarksByExam(@Param("examId") Long examId);
	
	@Query("""
			    SELECT COUNT(r) FROM Result r
			    WHERE r.exam.id = :examId AND r.status = 'PASS'
			""")
	long countPassByExam(@Param("examId") Long examId);
	
	@Query("""
			    SELECT COUNT(r) FROM Result r WHERE r.exam.id = :examId
			""")
	long countTotalByExam(@Param("examId") Long examId);
	
	Page<Result> findByExamId(Long examId, Pageable pageable);
	
	Page<Result> findByStudentId(Long studentId, Pageable pageable);
}