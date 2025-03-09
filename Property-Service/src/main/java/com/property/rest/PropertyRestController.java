package com.property.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.property.entity.Property;
import com.property.payload.PropertyDetailDto;
import com.property.payload.SearchDto;
import com.property.service.PropertyService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/property")
@AllArgsConstructor
public class PropertyRestController {

	private final PropertyService propertyService;
	
	@PostMapping(value = "/add-property")
	public Property addProperty(
			@RequestBody PropertyDetailDto propertyDetailDto
			) {
		return propertyService.addProperty(propertyDetailDto.getProperty(), propertyDetailDto);
	}
	
	
	@GetMapping(value = "/by-id")
	public Property getPropertyById(@RequestParam String propertyId) {
		return propertyService.getPropertyById(propertyId);
	}
	
	@PatchMapping(value = "/click/{propertyId}")
	public Boolean updatePropertyClick(@PathVariable String propertyId) {
		return propertyService.updatePropertyClick(propertyId);
	}
	
	@DeleteMapping(value = "/delete")
	public String deleteProperty(@RequestParam String propertyId) {
		return propertyService.deleteProperty(propertyId);
	}
	
	@PutMapping(value = "/update/{propertyId}")
	public Property updateProperty(@PathVariable String propertyId ,@RequestBody Property property) {
		return propertyService.updateProperty(propertyId, property);
	}
	
	@GetMapping(value = "/owner/{ownerUserId}")
	public List<Property> allPropertyByOwnerUserId(
		@PathVariable String ownerUserId
	){
		return propertyService.allPropertyByOwnerUserId(ownerUserId);
	}
	
	@PostMapping(value = "/search")
	public List<Property> searchProperty(@RequestBody SearchDto searchDto){
		return propertyService.searchProperty(searchDto);
	}
}
