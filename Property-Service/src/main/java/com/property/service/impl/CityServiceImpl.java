package com.property.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.property.entity.City;
import com.property.entity.Country;
import com.property.repository.CityRepository;
import com.property.repository.CountryRepository;
import com.property.service.CityService;
import com.property.service.CountryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService{
	
	private final CityRepository cityRepository;
	
	@Override
	public City createCity(City city) {
		return cityRepository.save(city);
	}

	@Override
	public List<City> allCity() {
		return cityRepository.findAll();
	}

}
