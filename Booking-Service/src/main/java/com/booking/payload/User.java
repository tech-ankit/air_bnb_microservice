package com.booking.payload;

import java.time.LocalDateTime;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private String userId;
	private String name;
	private String username;
	private String email;
	private String mobile;
	private String pazzwort;
	private String role;
	private LocalDateTime registrationDate;
	private LocalDateTime updateDate;

}
