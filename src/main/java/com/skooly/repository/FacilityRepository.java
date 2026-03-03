package com.skooly.repository;
import com.skooly.model.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
	@Query("""
			    SELECT f FROM Facility f
			    WHERE (:status IS NULL OR f.status = :status)
			    AND (:search IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Facility> findWithFilters(
			@Param("status") Facility.FacilityStatus status,
			@Param("search") String search,
			Pageable pageable);
}