package com.patsnap.inspad.core.dto;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class UserInfo {
	
	private String id;
	
	private String username;
	
	private String password;
	
	private String company;
	
	private String status;
	
	/** 用户状态：激活。 **/
    public static final String STATUS_ACTIVE = "ACTIVE";
    /** 用户状态：未激活。 **/
    public static final String STATUS_INACTIVE = "INACTIVE";
	
	private List<GrantedAuthority> authorities;
	
	public static UserInfo of(String id, String username, List<GrantedAuthority> authorities) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setUsername(username);
		userInfo.setAuthorities(authorities);
		return userInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
}
