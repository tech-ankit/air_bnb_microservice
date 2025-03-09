package com.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.user.constants.Role;

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
@Entity
@Table(name = "user_tbl")
public class User {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String userId;
	
	@Column(length = 100 , nullable = false)
	private String name;
	
	@Column(length = 100 ,unique = true , nullable = false)
	private String username;
	
	@Column(length = 100 ,unique = true , nullable = false)
	private String email;
	
	@Column(length = 15 ,unique = true , nullable = false)
	private String mobile;
	
	@Column(length = 1000, nullable = false)
	private String pazzwort;
	
	@Column(length = 20, nullable = false)
	private String role;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime registrationDate;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updateDate;

}
