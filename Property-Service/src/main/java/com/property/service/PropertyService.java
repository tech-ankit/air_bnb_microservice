package com.property.service;

import java.util.List;


import com.property.entity.Property;
import com.property.payload.PropertyDetailDto;
import com.property.payload.SearchDto;

public interface PropertyService {
	
	Property addProperty(Property property,PropertyDetailDto propertyDetailDto);
	Property getPropertyById(String propertyId);
	Boolean updatePropertyClick(String propertyId);
	String deleteProperty(String propertyId);
	Property updateProperty(String propertyId , Property property);
	List<Property> allPropertyByOwnerUserId(String ownerUserId);
	List<Property> searchProperty(SearchDto searchDto);
}
