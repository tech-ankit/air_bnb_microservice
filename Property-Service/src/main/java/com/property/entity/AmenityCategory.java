package com.property.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class AmenityCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long amenityCategoryId;
	
	@Column(nullable = false , length = 50 , unique = true)
	private String amenityCategoryName;
}
