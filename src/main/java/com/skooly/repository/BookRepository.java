package com.skooly.repository;
import com.skooly.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	boolean existsByIsbn(String isbn);
	
	@Query("""
			    SELECT b FROM Book b
			    WHERE (:search IS NULL OR LOWER(b.title)  LIKE LOWER(CONCAT('%', :search, '%'))
			                           OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')))
			    AND (:author   IS NULL OR LOWER(b.author)   LIKE LOWER(CONCAT('%', :author,   '%')))
			    AND (:category IS NULL OR LOWER(b.category) LIKE LOWER(CONCAT('%', :category, '%')))
			    AND (:available IS NULL OR (:available = TRUE AND b.availableCopies > 0))
			""")
	Page<Book> findWithFilters(
			@Param("search") String search,
			@Param("author") String author,
			@Param("category") String category,
			@Param("available") Boolean available,
			Pageable pageable
	                          );
}