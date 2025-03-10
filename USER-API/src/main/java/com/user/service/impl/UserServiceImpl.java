package com.user.service.impl;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.user.bindings.TokenResponseDto;
import org.apache.hc.client5.http.impl.Operations.CompletedFuture;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.bindings.LoginDto;
import com.user.bindings.UserDto;
import com.user.constants.AppConstant;
import com.user.constants.Role;
import com.user.entity.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import com.user.util.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenUtil tokenUtil;

	@Override
	public UserDto signUpUser(UserDto userDto,String role) {
		
		//Create Thread Pool With Size 2
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		
		//Give Method To One Thread 
		CompletableFuture<Optional<User>> byEmailFuture = CompletableFuture.supplyAsync(()->{
			Optional<User> byEmail = userRepository.findByEmail(userDto.getEmail());
			if(byEmail.isPresent()) {
				return byEmail;
			}else {
				return Optional.empty();
			}
		},executorService);
		
		//Give Method To Another Thread 
		CompletableFuture<Optional<User>> byUsernameFuture = CompletableFuture.supplyAsync(()->{
			Optional<User> byUsername = userRepository.findByUsername(userDto.getUsername());
			if(byUsername.isPresent()) {
				return byUsername;
			}else {
				return Optional.empty();
			}
		},executorService);
		try {
			CompletableFuture.allOf(byEmailFuture, byUsernameFuture).join();
			Optional<User> userByEmail = byEmailFuture.get();
			Optional<User> userByUsername = byUsernameFuture.get();
			if(userByEmail.isPresent() || userByUsername.isPresent()) {
				return null;
			}
		}catch(Exception e) {
			return null;
		}finally {
			executorService.shutdown();
		}

		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		if(AppConstant.USER.equalsIgnoreCase(role)) {
			user.setRole(Role.ROLE_USER);
		}else if(AppConstant.OWNER.equalsIgnoreCase(role)) {
			user.setRole(Role.ROLE_OWNER);
		}else if(AppConstant.ADMIN.equalsIgnoreCase(role)) {
			user.setRole(Role.ROLE_ADMIN);
		}
		user.setPazzwort(passwordEncoder.encode(userDto.getPazzwort()));
		userRepository.save(user);
		return userDto;
	}

	@Override
	public String loginUser(LoginDto loginDto) {
		Optional<User> byUsername = userRepository.findByUsername(loginDto.getUsername());
		if(byUsername.isPresent()) {
			User user = byUsername.get();
			if(passwordEncoder.matches(loginDto.getPazzwort(),user.getPazzwort())) {
				return tokenUtil.generateToken(user.getUsername());
			}
		}
		return null;
	}

	@Override
	public String deleteUser(String id) {
		userRepository.deleteById(id);
		return AppConstant.USER_DELETED_WITH_ID + id;
	}

	@Override
	public String updateUsername(String id, String updatedUsername) {
		Optional<User> byId = userRepository.findById(id);
		if(byId.isPresent()) {
			User user = byId.get();
			user.setUsername(updatedUsername);
			userRepository.save(user);
			return AppConstant.USER_UPDATED;
		}
		return AppConstant.SOMETHING_WENT_WRONG;
	}

	@Override
	public String updatePassword(String id, String updatedPassword) {
		Optional<User> byId = userRepository.findById(id);
		if(byId.isPresent()) {
			User user = byId.get();
			user.setPazzwort(updatedPassword);
			userRepository.save(user);
			return AppConstant.PASSWORD_UPDATED;
		}
		return AppConstant.SOMETHING_WENT_WRONG;
	}

	@Override
	public String updateEmail(String id, String updatedEmail) {
		Optional<User> byId = userRepository.findById(id);
		if(byId.isPresent()) {
			User user = byId.get();
			user.setEmail(updatedEmail);
			userRepository.save(user);
			return AppConstant.EMAIL_UPDATED;
		}
		return AppConstant.SOMETHING_WENT_WRONG;
	}

	@Override
	public String updateMobile(String id, String updatedMobile) {
		Optional<User> byId = userRepository.findById(id);
		if(byId.isPresent()) {
			User user = byId.get();
			user.setMobile(updatedMobile);
			userRepository.save(user);
			return AppConstant.MOBILE_UPDATED;
		}
		return AppConstant.SOMETHING_WENT_WRONG;
	}

	@Override
	public TokenResponseDto checkTokenValidity(String token) {
		String username = null;
		try {
			username = tokenUtil.getUsername(token);
		}catch(Exception e) {
			return null;
		}
		Optional<User> byUsername = userRepository.findByUsername(username);
		if(byUsername.isPresent()) {
			User user = byUsername.get();
			TokenResponseDto tokenResponseDto = new TokenResponseDto();
			BeanUtils.copyProperties(user,tokenResponseDto);
			return tokenResponseDto;
		}else {
			return null;
		}
	}

	@Override
	public UserDto getByUserId(String userId) {
		Optional<User> opUser = userRepository.findById(userId);
		if(opUser.isPresent()) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(opUser.get(), userDto);
			return userDto;
		}
		return null;
	}

}
