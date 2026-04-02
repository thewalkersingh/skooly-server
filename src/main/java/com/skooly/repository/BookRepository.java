package com.skooly.repository;
import com.skooly.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findBySchoolId(Long schoolId);
	
	Optional<Book> findByIdAndSchoolId(Long id, Long schoolId);
	
	long countBySchoolId(Long schoolId);
	
	@Query("SELECT b FROM Book b WHERE b.school.id = :schoolId AND " +
	       "(LOWER(b.title)    LIKE LOWER(CONCAT('%', :q, '%')) OR " +
	       " LOWER(b.author)   LIKE LOWER(CONCAT('%', :q, '%')) OR " +
	       " LOWER(b.isbn)     LIKE LOWER(CONCAT('%', :q, '%')) OR " +
	       " LOWER(b.category) LIKE LOWER(CONCAT('%', :q, '%')))")
	List<Book> search(@Param("schoolId") Long schoolId, @Param("q") String q);
	
}