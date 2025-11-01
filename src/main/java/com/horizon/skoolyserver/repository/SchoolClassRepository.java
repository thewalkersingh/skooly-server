package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
	List<SchoolClass> findByGrade(String grade);
}