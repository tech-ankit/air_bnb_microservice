package com.user.rest;

import com.user.bindings.TokenResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.bindings.LoginDto;
import com.user.bindings.TokenDto;
import com.user.bindings.UserDto;
import com.user.constants.AppConstant;
import com.user.service.OtpService;
import com.user.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserRestController {
	
	private final UserService userService;
	private final OtpService otpService;
	
	public UserRestController(UserService userService,OtpService otpService) {
		this.userService = userService;
		this.otpService = otpService;
	}
	
	@GetMapping
	public String getMsg() {
		return "Worked";
	}
	
	@PostMapping(value = "/sign-up")
	@CircuitBreaker(fallbackMethod = "signUpUserFallBack", name = SIGN_UP_USER)
	public UserDto signUpUser(@RequestBody UserDto userDto,@RequestParam(value = "role", defaultValue = "user") String role) {
		return userService.signUpUser(userDto, role);
	}
	
	@GetMapping(value = "/{userId}")
	public UserDto getUserById(@PathVariable String userId) {
		return userService.getByUserId(userId);
	}
	
	@PostMapping(value = "/login")
	@CircuitBreaker(fallbackMethod = LOGIN_USER_FALLBACK ,name = LOGIN_USER)
	public TokenDto loginUser(@RequestBody LoginDto loginDto) {
		String token = userService.loginUser(loginDto);
		if(token == null) {
			return null;
		}{
			return new TokenDto(token);
		}
	}
	
	@DeleteMapping(value = "/delete/{id}")
	@CircuitBreaker(fallbackMethod = DELETE_USER_FALLBACK,name = DELETE_USER )
	public String deleteUser(@PathVariable(value = "id") String id) {
		return userService.deleteUser(id);
	}
	
	@PatchMapping(value = "/update/username")
	@CircuitBreaker(fallbackMethod = UPDATE_USERNAME_FALLBACK,name = UPDATE_USERNAME )
	public String updateUsername(
			@RequestParam(value = "id") String id,
			@RequestParam(value = "username") String updatedUsername
			) {
		return userService.updateUsername(id , updatedUsername);
	}
	
	@PatchMapping(value = "/update/password")
	@CircuitBreaker(fallbackMethod = UPDATE_PASSWORD_FALLBACK,name = UPDATE_PASSWORD )
	public String updatePassword(
			@RequestParam(value = "id") String id,
			@RequestParam(value = "password") String updatedPassword
			) {
		return userService.updatePassword(id , updatedPassword);
	}
	
	@PatchMapping(value = "/update/email")
	@CircuitBreaker(fallbackMethod = UPDATE_EMAIL_FALLBACK,name = UPDATE_EMAIL )
	public String updateEmail(
			@RequestParam String id,
			@RequestParam(value= "email") String updatedEmail
			) {
		return userService.updateEmail(id , updatedEmail);
	}

	@PatchMapping(value = "/update/mobile")
	@CircuitBreaker(fallbackMethod = UPDATE_MOBILE_FALLBACK,name = UPDATE_MOBILE)
	public String updateMobile(
			@RequestParam(value = "id") String id,
			@RequestParam(value = "mobile") String updatedMobile
			) {
		return userService.updateMobile(id , updatedMobile);
	}
	
	@GetMapping(value = "/token/validation")
	@CircuitBreaker(fallbackMethod = CHECK_TOKEN_VALIDITY_FALLBACK, name = TOKEN_VALIDITY)
	public ResponseEntity<TokenResponseDto> checkTokenValidity(@RequestParam(value = "token") String token){
		TokenResponseDto tokenResponseDto = userService.checkTokenValidity(token);
		return ResponseEntity.ok(tokenResponseDto);
	}
	
	@GetMapping(value = "/otp")
	public String generateOtp(@RequestParam String userId){
		String otp = otpService.generateOtp(userId);
		return otpService.getOtpMessage(otp);
	}
	
	@GetMapping(value = "/otp/validate")
	public Boolean validateOtp(@RequestParam String userId,@RequestParam String otp){
		Boolean isValid = otpService.validateOtp(otp ,userId);
		return isValid;
	}
	
	
	//FALL BACK METHODS
	
	public String updatePasswordFallBack(Throwable t) {
		return AppConstant.PASSWORD_CANNOT_UPDATE_MESSAGE;
	}
	
	public UserDto signUpUserFallBack(UserDto userDto, String errorMessage,Throwable t) {
		return new UserDto(null,null,null,null,null);
	}
	
	public String loginUserFallBack(Throwable t) {
		return AppConstant.LOGIN_FALLBACK_MESSAGE;
	}

	
	public String deleteFallBack(Throwable t) {
		return AppConstant.USER_CANNOT_DELETE_MESSAGE;
	}
	
	public String updateEmailFallBack(Throwable t) {
		return AppConstant.EMAIL_CANNOT_UPDATE_MESSAGE;
	}
	
	public String updateUsernameFallBack(Throwable t) {
		return AppConstant.USERNAME_CANNOT_UPDATE_MESSAGE;
	}
	
	public String updateMobileFallBack(Throwable t) {
		return AppConstant.MOBILE_CANNOT_UPDATE_MESSAGE;
	}
	
	public Boolean checkTokenValidityFallBack(Throwable t) {
		return false;
	}
	
	
	// CIRCUIT BREAKER NAME CONSTANT
	private static final String SIGN_UP_USER = "Sign-Up-User";
	private static final String LOGIN_USER = "Login-User";
	private static final String DELETE_USER = "delete-user";
	private static final String UPDATE_USERNAME = "update-username";
	private static final String UPDATE_EMAIL = "update-email";
	private static final String UPDATE_MOBILE = "update-mobile";
	private static final String UPDATE_PASSWORD = "Sign-Up-User";
	private static final String TOKEN_VALIDITY = "token-validity";

	// CIRCUIT BREAKER METHOD NAME CONSTANT
//	private static final String SIGN_UP_USER_FALLBACK = "signUpUserFallBack";
	private static final String LOGIN_USER_FALLBACK = "loginUserFallBack";
	private static final String DELETE_USER_FALLBACK = "deleteFallBack";
	private static final String UPDATE_USERNAME_FALLBACK = "updateUsernameFallBack";
	private static final String UPDATE_PASSWORD_FALLBACK = "updatePasswordFallBack";
	private static final String UPDATE_EMAIL_FALLBACK = "updateEmailFallBack";
	private static final String UPDATE_MOBILE_FALLBACK = "updateMobileFallBack";
	private static final String CHECK_TOKEN_VALIDITY_FALLBACK = "checkTokenValidityFallBack";
}
