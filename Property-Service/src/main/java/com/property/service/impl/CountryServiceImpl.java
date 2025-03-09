package com.property.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.property.entity.Country;
import com.property.repository.CountryRepository;
import com.property.service.CountryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService{
	
	private final CountryRepository countryRepository;
	
	@Override
	public Country createCountry(Country country) {
		return countryRepository.save(country);
	}

	@Override
	public List<Country> allCountry() {
		return countryRepository.findAll();
	}

}
