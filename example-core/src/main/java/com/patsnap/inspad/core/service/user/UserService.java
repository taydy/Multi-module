package com.patsnap.inspad.core.service.user;

import org.springframework.stereotype.Service;

import com.patsnap.inspad.core.dto.UserInfo;

@Service
public class UserService {
	
	public UserInfo getByUsername(String username) {
		return new UserInfo();
	}

}
