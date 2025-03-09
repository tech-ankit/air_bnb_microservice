package com.property.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PropertyImageCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long propertyImageCategoryId;
	
	@Column(nullable = false , unique = true , length = 20 )
	private String categoryName;

}
