package com.user.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.user.annotation.JwtService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@JwtService
public class TokenUtil {
	
	@Value("${jwt.key}")
	private String key;
	
	@Value("${jwt.issuer}")
	private String issuer;
	
	@Value("${jwt.expiry}")
	private Long expiry;
	
	private Algorithm algorithm;
	
	@PostConstruct
	private void init() {
		algorithm = Algorithm.HMAC256(key);
	}

	public String generateToken(String username) {
		return JWT.create()
				.withClaim("username", username)
				.withExpiresAt(new Date(System.currentTimeMillis()+expiry))
				.withIssuer(issuer)
				.sign(algorithm);
	}
	
	public String getUsername(String token) {
		return JWT.require(algorithm)
				.withIssuer(issuer)
				.build()
				.verify(token)
				.getClaim("username")
				.asString();
	}
	
}
