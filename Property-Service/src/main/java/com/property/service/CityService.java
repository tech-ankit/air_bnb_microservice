package com.property.service;

import java.util.List;

import com.property.entity.City;

public interface CityService {

	City createCity(City city);
	List<City> allCity();
}
