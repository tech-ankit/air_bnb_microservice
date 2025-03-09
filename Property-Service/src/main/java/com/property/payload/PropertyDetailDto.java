package com.property.payload;

import com.property.entity.Property;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyDetailDto {
	
	private Long countryId;
	private Long cityId;
	private Long stateId;
	private Long locationId;
	private long categoryId;
	private String userId;
	private Property property;

}
