package com.skooly.repository;
import com.skooly.model.BookIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
	Page<BookIssue> findByStudentId(Long studentId, Pageable pageable);
	
	Page<BookIssue> findByStatus(BookIssue.IssueStatus status, Pageable pageable);
	
	@Query("""
			    SELECT i FROM BookIssue i
			    WHERE i.status = 'ISSUED'
			    AND i.dueDate < :today
			""")
	List<BookIssue> findOverdueBooks(@Param("today") LocalDate today);
	
	@Query("""
			    SELECT i FROM BookIssue i
			    WHERE i.student.id = :studentId
			    AND i.status = 'ISSUED'
			""")
	List<BookIssue> findActiveIssuesByStudent(@Param("studentId") Long studentId);
	
	boolean existsByBookIdAndStudentIdAndStatus(Long bookId, Long studentId, BookIssue.IssueStatus status);
}