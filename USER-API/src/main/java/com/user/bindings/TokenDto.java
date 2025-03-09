package com.user.bindings;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDto {
	
	private String token;
	private String refreshToken;
	private String tokenType;

	public TokenDto(String token) {
		this.token = token;
		this.refreshToken = "REFRESH_TOKEN";
		this.tokenType = "JWT";
	}
}
