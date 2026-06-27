package com.skooly.service;

import com.skooly.dto.request.UserIdentityRequest;
import com.skooly.dto.response.UserIdentityResponse;

public interface UserIdentityService {
	
	// ── Create / Update ───────────────────────────────────────────────────────
	// Typically called internally by Teacher/Student/Parent services via cascade,
	// but exposed here for standalone identity management if needed.
	UserIdentityResponse createIdentity(UserIdentityRequest request);
	
	UserIdentityResponse updateIdentity(Long identityId, UserIdentityRequest request);
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	UserIdentityResponse getIdentity(Long identityId);
	
	UserIdentityResponse getIdentityByPhone(String phone);
	
	UserIdentityResponse getIdentityByEmail(String email);
	
	// ── Existence checks (use in Teacher/Student/Parent services before save) ──
	boolean existsByPhone(String phone);
	
	boolean existsByEmail(String email);
	
}