package com.skooly.service.impl;

import com.skooly.entity.User;
import com.skooly.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	public boolean existsBySchoolIdAndUsername(Long schoolId, String username) {
		return false;
	}
	
	public Optional<User> findByUsername(String username) {
		return Optional.empty();
	}
	
	public List<Optional<User>> findByUserType(Long userType) {
		return List.of();
	}
	
}