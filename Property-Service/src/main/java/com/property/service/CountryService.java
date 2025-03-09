package com.property.service;

import java.util.List;

import com.property.entity.Country;

public interface CountryService {

	Country createCountry(Country country);
	List<Country> allCountry();
}
