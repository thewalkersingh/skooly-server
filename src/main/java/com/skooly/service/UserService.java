package com.skooly.service;

import com.skooly.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
	
	boolean existsBySchoolIdAndUsername(Long schoolId, String username);
	
	Optional<User> findByUsername(String username);
	
	List<Optional<User>> findByUserType(Long userType);
	
}