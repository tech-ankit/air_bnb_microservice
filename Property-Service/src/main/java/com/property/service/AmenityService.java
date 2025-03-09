package com.property.service;

import java.util.List;
import java.util.Map;

import com.property.entity.Amenity;
import com.property.entity.AmenityCategory;

public interface AmenityService {
	
	AmenityCategory addAmenityCategory(AmenityCategory amenityCategory);
	List<AmenityCategory> getAllCategory();
	String addPropertyAmenity(String propertyId,long amenityId);
	Amenity addAmenity(Amenity amenity,Long amenityCategoryId);
	List<Amenity> allAmenities();
	String deleteAmeity(Long amenityId);
	Map<String,List<Amenity>> listAmenityByProperty(String propertyId);
}
