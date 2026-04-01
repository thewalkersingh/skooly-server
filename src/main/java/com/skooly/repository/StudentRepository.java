package com.skooly.repository;
import com.skooly.constant.Status;
import com.skooly.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findBySchoolId(Long schoolId);
	
	Optional<Student> findByIdAndSchoolId(Long id, Long schoolId);
	
	List<Student> findBySchoolIdAndSchoolClassId(Long schoolId, Long classId);
	
	List<Student> findBySchoolIdAndSchoolClassIdAndSectionId(Long schoolId, Long classId, Long sectionId);
	
	long countBySchoolId(Long schoolId);
	
	long countBySchoolIdAndStatus(Long schoolId, Status status);
	
	// searching by firstname, lastname or email
	@Query("SELECT s FROM Student s WHERE s.school.id = :schoolId AND " +
	       "(LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
	       " LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :query, '%')) OR " +
	       " LOWER(s.email)     LIKE LOWER(CONCAT('%', :query, '%')))")
	List<Student> searchBySchoolId(@Param("schoolId") Long schoolId, @Param("query") String query);
	
}
