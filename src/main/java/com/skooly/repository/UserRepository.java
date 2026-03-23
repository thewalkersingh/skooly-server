package com.skooly.repository;

import com.skooly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsBySchoolIdAndUsername(Long schoolId, String username);
    Optional<User> findBySchoolIdAndUsername(Long schoolId, String username);
}
