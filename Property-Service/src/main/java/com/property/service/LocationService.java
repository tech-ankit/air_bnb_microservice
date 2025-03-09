package com.property.service;

import java.util.List;

import com.property.entity.Location;

public interface LocationService {

	Location createLocation(Location location);
	List<Location> allLocation();
}
