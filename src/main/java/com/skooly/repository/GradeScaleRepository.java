package com.skooly.repository;
import com.skooly.model.GradeScale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface GradeScaleRepository extends JpaRepository<GradeScale, Long> {
	@Query("""
			    SELECT g FROM GradeScale g
			    WHERE :marks >= g.minMarks AND :marks <= g.maxMarks
			""")
	Optional<GradeScale> findByMarks(@Param("marks") BigDecimal marks);
}