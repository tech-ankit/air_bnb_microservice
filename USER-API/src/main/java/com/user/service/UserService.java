package com.user.service;

import com.user.bindings.LoginDto;
import com.user.bindings.UserDto;

public interface UserService {
	
	UserDto signUpUser(UserDto userDto,String role);
	String loginUser(LoginDto loginDto);
	String deleteUser(String id);
	String updateUsername(String id, String updatedUsername);
	String updatePassword(String id, String updatedPassword);
	String updateEmail(String id, String updatedEmail);
	String updateMobile(String id, String updatedMobile);
	Boolean checkTokenValidity(String token);
	UserDto getByUserId(String userId);
}
