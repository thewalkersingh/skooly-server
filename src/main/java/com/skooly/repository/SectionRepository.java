package com.skooly.repository;

import com.skooly.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findBySchoolIdAndSchoolClassId(Long schoolId, Long classId);
    List<Section> findBySchoolId(Long schoolId);
}
