package com.property.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.entity.Country;
import com.property.service.CountryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/country")
@AllArgsConstructor
public class CountryRestController {
	
	private final CountryService countryService;
	

	@PostMapping(value = "/add")
	public Country addCountry(@RequestBody Country country) {
		return countryService.createCountry(country);
	}
	
	@GetMapping(value = "/all-country")
	public List<Country> getAllCountry(){
		return countryService.allCountry();
	}
	
}
