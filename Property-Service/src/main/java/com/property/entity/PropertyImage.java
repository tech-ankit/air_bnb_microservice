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
public class PropertyImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long propertyImageId;
	
	@Column(length = 30 , nullable = false)
	private String imageTitle;
	
	@Column(length = 1000)
	private String imageDescription;
	
	@Column(length = 1000)
	private String imageKey;
	
	@Column(nullable = false , length = 1000 , unique = true)
	private String imageUrl;
	
	@ManyToOne
	@JoinColumn(nullable = false , name = "property_id")
	private Property property;
	
	@ManyToOne
	@JoinColumn(nullable = false , name = "property_image_category_id")
	private PropertyImageCategory propertyImageCategory;

}
