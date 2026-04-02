package com.skooly.repository;
import com.skooly.model.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
	List<BookIssue> findBySchoolId(Long schoolId);
	
	Optional<BookIssue> findByIdAndSchoolId(Long id, Long schoolId);
	
	List<BookIssue> findBySchoolIdAndStatus(Long schoolId, BookIssue.Status status);
	
	@Query("SELECT bi FROM BookIssue bi WHERE bi.school.id = :schoolId AND " +
	       "(LOWER(bi.book.title)          LIKE LOWER(CONCAT('%', :q, '%')) OR " +
	       " LOWER(bi.student.firstName)   LIKE LOWER(CONCAT('%', :q, '%')) OR " +
	       " LOWER(bi.student.lastName)    LIKE LOWER(CONCAT('%', :q, '%')))")
	List<BookIssue> search(@Param("schoolId") Long schoolId, @Param("q") String q);
	
	// Check if student already has this book issued
	boolean existsByBookIdAndStudentIdAndStatus(Long bookId, Long studentId, BookIssue.Status status);
	
}