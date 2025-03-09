package com.booking.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

	private String userId;
	private String name;
	private String username;
	private String email;
	private String mobile;

}
