package com.property.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.property.entity.Amenity;
import com.property.entity.AmenityCategory;
import com.property.entity.Property;
import com.property.entity.PropertyAmenity;
import com.property.repository.AmenityCategoryRepository;
import com.property.repository.AmenityRepository;
import com.property.repository.PropertyAmenityRepository;
import com.property.repository.PropertyRepository;
import com.property.service.AmenityService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AmenityServiceImpl implements AmenityService {
	
	private final AmenityRepository amenityRepository;
	private final PropertyAmenityRepository propertyAmenityRepository;
	private final AmenityCategoryRepository amenityCategoryRepository;
	private final PropertyRepository propertyRepository;

	@Override
	public Amenity addAmenity(Amenity amenity, Long amenityCategoryId) {
		Optional<AmenityCategory> byId = amenityCategoryRepository.findById(amenityCategoryId);
		if(byId.isPresent()){
			amenity.setAmenityCategory(byId.get());
			return amenityRepository.save(amenity);
		}
		return null;
	}

	@Override
	public List<Amenity> allAmenities() {
		return amenityRepository.findAll();
	}

	@Override
	public String deleteAmeity(Long amenityId) {
		Optional<Amenity> byId = amenityRepository.findById(amenityId);
		if(byId.isPresent()) {
			amenityRepository.delete(byId.get());
			byId = amenityRepository.findById(amenityId);
			if(byId.isEmpty()) {
				return "Amenity Deleted";
			}
		}
		return "Amenity Not Delete Due to Some Issue";
	}

	@Override
	public Map<String,List<Amenity>> listAmenityByProperty(String propertyId) {
		List<Amenity> amenities = propertyAmenityRepository.findByProperty(propertyId);
		Map<String, List<Amenity>> collect = amenities.stream().collect(Collectors.groupingBy(a->a.getAmenityCategory().getAmenityCategoryName()));
		System.out.println(collect);
		return collect;
	}

	@Override
	public AmenityCategory addAmenityCategory(AmenityCategory amenityCategory) {
		return amenityCategoryRepository.save(amenityCategory);
	}

	@Override
	public List<AmenityCategory> getAllCategory() {
		return amenityCategoryRepository.findAll();
	}

	@Override
	public String addPropertyAmenity(String propertyId, long amenityId) {
		Optional<Property> opProperty = propertyRepository.findById(propertyId);
		Optional<Amenity> opAmenity = amenityRepository.findById(amenityId);
		if(opProperty.isPresent() && opAmenity.isPresent()) {
			PropertyAmenity propertyAmenity = new PropertyAmenity();
			propertyAmenity.setProperty(opProperty.get());
			propertyAmenity.setAmenity(opAmenity.get());
			propertyAmenity = propertyAmenityRepository.save(propertyAmenity);
			if(propertyAmenity.getId() != null) {
				return "Property Amenity Is Added";
			}
		}
		return "Property Amenity Not Added Due To Some Issue";
	}

}
