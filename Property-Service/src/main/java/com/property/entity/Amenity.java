package com.property.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Amenity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long amenityId;
	
	@Column(nullable = false , length = 20 , unique = true)
	private String amenityName;
	
	@ManyToOne
	@JoinColumn(name = "amenity_category_id")
	private AmenityCategory amenityCategory;
	

}
