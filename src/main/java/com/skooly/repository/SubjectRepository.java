package com.skooly.repository;

import com.skooly.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
	List<Subject> findBySchoolId(Long schoolId);
}