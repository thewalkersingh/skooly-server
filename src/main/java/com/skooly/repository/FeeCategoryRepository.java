package com.skooly.repository;
import com.skooly.model.FeeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeCategoryRepository extends JpaRepository<FeeCategory, Long> {
	boolean existsByName(String name);
}