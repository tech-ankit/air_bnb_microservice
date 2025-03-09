package com.user.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.user.bindings.OtpDetails;
import com.user.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService {

	private static final String CHARACTERS = "0123456789"; // Only digits for OTP
	private static final int OTP_LENGTH = 6;
	
	Map<String, OtpDetails> map = new HashMap<>();

	@Override
	public String generateOtp(String userId) {
		SecureRandom random = new SecureRandom();
		StringBuilder otp = new StringBuilder(OTP_LENGTH);

		for (int i = 0; i < OTP_LENGTH; i++) {
			int index = random.nextInt(CHARACTERS.length());
			otp.append(CHARACTERS.charAt(index));
		}
		 String otpStr = otp.toString();
		 OtpDetails otpDetails = new OtpDetails();
		 otpDetails.setOtp(otpStr);
		 otpDetails.setUserId(userId);
		 otpDetails.setExpiry(System.currentTimeMillis()+30000);
		 map.put(userId,otpDetails);
		 return otpStr;
	}

	@Override
	public Boolean validateOtp(String otp,String userId) {
        OtpDetails storedOtpDetails = map.get(userId);
        if (storedOtpDetails == null) {
            System.out.println("OTP not found for user " + userId);
            return false;
        }
        
        if (System.currentTimeMillis() > storedOtpDetails.getExpiry()) {
            System.out.println("OTP expired for user " + userId);
            map.remove(userId);
            return false;
        }

        if (otp.equals(storedOtpDetails.getOtp())) {
            map.remove(userId);
            return true;
        } else {
            return false;
        }
	}

	
	@Override
	public String getOtpMessage(String otp) {
		return String.format("%s is your verification code. For your security, do not share this code.", otp);
	}

}
