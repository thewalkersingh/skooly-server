package com.skooly.repository;
import com.skooly.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	List<Teacher> findBySchoolId(Long schoolId);
	
	Optional<Teacher> findByIdAndSchoolId(Long id, Long schoolId);
	
	long countBySchoolId(Long schoolId);
	
	@Query("SELECT t FROM Teacher t WHERE t.school.id = :schoolId AND "+
	       "(LOWER(t.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR "+
	       " LOWER(t.lastName)  LIKE LOWER(CONCAT('%', :query, '%')) OR "+
	       " LOWER(t.email)     LIKE LOWER(CONCAT('%', :query, '%')))")
	List<Teacher> searchBySchoolId(@Param("schoolId") Long schoolId, @Param("query") String query);
}
