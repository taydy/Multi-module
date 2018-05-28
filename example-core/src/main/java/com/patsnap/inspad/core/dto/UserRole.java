package com.patsnap.inspad.core.dto;

public enum UserRole {
	
	COMMON, AMDIN;
	
	public String authority() {
		return "ROLE_" + this.name();
	}

}
