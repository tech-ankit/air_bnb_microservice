package com.booking.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.booking.payload.User;
import com.booking.payload.UserDto;

@FeignClient(name = "USER-API")
public interface UserFeign {
	
	@GetMapping(value = "/api/v1/user/{userId}")
	ResponseEntity<User> invokeGetUserById(@PathVariable String userId);

}
