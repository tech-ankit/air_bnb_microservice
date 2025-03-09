package com.property.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.property.entity.City;
import com.property.entity.Country;
import com.property.entity.Location;
import com.property.repository.CityRepository;
import com.property.repository.CountryRepository;
import com.property.repository.LocationRepository;
import com.property.service.CityService;
import com.property.service.CountryService;
import com.property.service.LocationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService{
	
	private final LocationRepository locationRepository;
	
	@Override
	public Location createLocation(Location location) {
		return locationRepository.save(location);
	}

	@Override
	public List<Location> allLocation() {
		return locationRepository.findAll();
	}

}
