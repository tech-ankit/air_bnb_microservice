package com.property.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.entity.City;
import com.property.entity.Country;
import com.property.entity.Location;
import com.property.service.CityService;
import com.property.service.CountryService;
import com.property.service.LocationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/location")
@AllArgsConstructor
public class LocationRestController {
	
	private final LocationService locationService;
	

	@PostMapping(value = "/add")
	public Location addCity(@RequestBody Location location) {
		return locationService.createLocation(location);
	}
	
	@GetMapping(value = "/all-location")
	public List<Location> getAllLocation(){
		return locationService.allLocation();
	}
	
}
