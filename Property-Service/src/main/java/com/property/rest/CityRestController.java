package com.property.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.entity.City;
import com.property.entity.Country;
import com.property.service.CityService;
import com.property.service.CountryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/city")
@AllArgsConstructor
public class CityRestController {
	
	private final CityService cityService;
	

	@PostMapping(value = "/add")
	public City addCity(@RequestBody City city) {
		return cityService.createCity(city);
	}
	
	@GetMapping(value = "/all-city")
	public List<City> getAllCity(){
		return cityService.allCity();
	}
	
}
