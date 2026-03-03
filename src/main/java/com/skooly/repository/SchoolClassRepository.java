
package com.skooly.repository;
import com.skooly.model.SchoolClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
	boolean existsByName(String name);
	
	@Query("""
			    SELECT c FROM SchoolClass c
			    WHERE (:search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<SchoolClass> findWithFilters(@Param("search") String search, Pageable pageable);
}