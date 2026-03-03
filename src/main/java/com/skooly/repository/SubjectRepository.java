
package com.skooly.repository;
import com.skooly.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
	boolean existsByCode(String code);
	
	List<Subject> findByTeachersSchoolClassId(Long classId);
	
	@Query("""
			    SELECT s FROM Subject s
			    WHERE (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
			           OR LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Subject> findWithFilters(@Param("search") String search, Pageable pageable);
}