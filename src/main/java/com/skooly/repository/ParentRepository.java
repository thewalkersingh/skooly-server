package com.skooly.repository;
import com.skooly.model.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
	Optional<Parent> findByUserId(Long userId);
	
	boolean existsByEmail(String email);
	
	@Query("""
			    SELECT p FROM Parent p
			    WHERE (:search IS NULL
			           OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
			           OR LOWER(p.lastName)  LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Parent> findWithFilters(@Param("search") String search, Pageable pageable);
}