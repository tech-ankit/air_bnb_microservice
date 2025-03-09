package com.user.bindings;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OtpDetails {

	private String userId;
	private String otp;
	private Long expiry;
}
