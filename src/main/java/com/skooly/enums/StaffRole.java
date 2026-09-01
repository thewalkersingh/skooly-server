package com.skooly.enums;

public enum StaffRole {
	ADMIN,
	STUDENT,
	TEACHER,
	LIBRARIAN,
	HELPER,
	ACCOUNTANT,
	LAB_ASSISTANT,
	DRIVER,
	SECURITY_GUARD;
	
	public static StaffRole fromOrdinal(long role) {
		return StaffRole.values()[(int) role - 1];
	}
}