package com.skooly.utils;
/**
 * Thread-local holder for the current school (tenant) ID.
 * Set per-request once JWT is wired in; for now used for explicit school-scoped queries.
 */
public class TenantContext {
	private static final ThreadLocal<Long> CURRENT_SCHOOL = new ThreadLocal<>();
	
	private TenantContext() { }
	
	public static void setSchoolId(Long schoolId) {
		CURRENT_SCHOOL.set(schoolId);
	}
	
	public static Long getSchoolId() {
		return CURRENT_SCHOOL.get();
	}
	
	public static void clear() {
		CURRENT_SCHOOL.remove();
	}
}
