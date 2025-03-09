package com.property.rest;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.property.entity.Amenity;
import com.property.entity.AmenityCategory;
import com.property.service.AmenityService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/amenity")
public class AmenityRestController {
	
	private final AmenityService amenityService;
	
	@PostMapping(value = "/add-category")
	public AmenityCategory addAmenityCategory(@RequestBody AmenityCategory amenityCategory) {
		return amenityService.addAmenityCategory(amenityCategory);
	}
	
	@PostMapping(value = "/property/add")
	public String addPropertyAmenity(@RequestParam String propertyId,@RequestParam long amenityId) {
		return amenityService.addPropertyAmenity(propertyId, amenityId);
	}
	
	@GetMapping(value = "/all-categories")
	public List<AmenityCategory> getAllCategory(){
		return amenityService.getAllCategory();
	}
	
	@PostMapping(value = "/add")
	public Amenity addAmenity(@RequestBody Amenity amenity,@RequestParam long amenityCategoryId) {
		return amenityService.addAmenity(amenity,amenityCategoryId);
	}
	
	@GetMapping(value = "/all-amenities")
	public List<Amenity> allAmenities(){
		return amenityService.allAmenities();
	}
	
	@DeleteMapping(value = "/delete/{amenityId}")
	public String deleteAmeity(@PathVariable Long amenityId) {
		return amenityService.deleteAmeity(amenityId);
	}
	
	@GetMapping(value = "/property/{propertyId}")
	public Map<String,List<Amenity>> listAmenityByProperty(@PathVariable String propertyId){
		return amenityService.listAmenityByProperty(propertyId);
	}

}
