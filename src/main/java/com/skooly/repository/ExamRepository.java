package com.skooly.repository;
import com.skooly.model.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
	@Query("""
			    SELECT e FROM Exam e
			    WHERE (:classId IS NULL OR e.schoolClass.id = :classId)
			    AND (:subjectId IS NULL OR e.subject.id = :subjectId)
			    AND (:academicYear IS NULL OR e.academicYear = :academicYear)
			    AND (:search IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Exam> findWithFilters(
			@Param("classId") Long classId,
			@Param("subjectId") Long subjectId,
			@Param("academicYear") String academicYear,
			@Param("search") String search,
			Pageable pageable
	                          );
}