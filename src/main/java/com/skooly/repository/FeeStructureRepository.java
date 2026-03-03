package com.skooly.repository;
import com.skooly.model.FeeStructure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
	Page<FeeStructure> findBySchoolClassId(Long classId, Pageable pageable);
	
	Page<FeeStructure> findByAcademicYear(String academicYear, Pageable pageable);
	
	List<FeeStructure> findBySchoolClassIdAndAcademicYear(Long classId, String academicYear);
}