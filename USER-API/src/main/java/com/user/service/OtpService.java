package com.user.service;

public interface OtpService {
	
	String generateOtp(String userId);
	Boolean validateOtp(String otp ,String userId);
	String getOtpMessage(String otp);
}
