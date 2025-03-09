package com.booking.feign;

import java.io.File;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.booking.entity.Property;
import com.booking.entity.Room;

@FeignClient(name = "PROPERTY-API")
public interface PropertyFeign {

	@GetMapping(value = "/api/v1/room/by-id")
	ResponseEntity<Room> invokeGetRoomById(@RequestParam Long roomId);
	
	@GetMapping(value = "/api/v1/property/by-id")
	Optional<Property> invokeGetPropertyById(@RequestParam String propertyId);
}
