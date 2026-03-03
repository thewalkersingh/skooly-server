
package com.skooly.repository;
import com.skooly.model.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
	List<Section> findBySchoolClassId(Long classId);
	
	Page<Section> findBySchoolClassId(Long classId, Pageable pageable);
	
	boolean existsByNameAndSchoolClassId(String name, Long classId);
}