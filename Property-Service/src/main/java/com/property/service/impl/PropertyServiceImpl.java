package com.property.service.impl;

import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.property.entity.City;
import com.property.entity.Country;
import com.property.entity.Location;
import com.property.entity.Property;
import com.property.entity.PropertyCategory;
import com.property.entity.Room;
import com.property.entity.RoomCategory;
import com.property.entity.State;
import com.property.payload.PropertyDetailDto;
import com.property.payload.SearchDto;
import com.property.repository.CityRepository;
import com.property.repository.CountryRepository;
import com.property.repository.LocationRepository;
import com.property.repository.PropertyCategoryRepository;
import com.property.repository.PropertyRepository;
import com.property.repository.RoomCategoryRepository;
import com.property.repository.RoomRepository;
import com.property.repository.StateRepository;
import com.property.service.PropertyService;

import lombok.AllArgsConstructor;

@Slf4j
@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {
	
	private final PropertyRepository propertyRepository;
	private final PropertyCategoryRepository propertyCategoryRepository;
	private final CountryRepository countryRepository;
	private final StateRepository stateRepository;
	private final LocationRepository locationRepository;
	private final CityRepository cityRepository;
	private final RoomCategoryRepository roomCategoryRepository;
	private final RoomRepository roomRepository;

	@Override
	public Property addProperty(Property property, PropertyDetailDto propertyDetailDto) {
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		CompletableFuture<PropertyCategory> propertyCategoryFuture = CompletableFuture.supplyAsync(()->{
			return propertyCategoryRepository
					.findById(propertyDetailDto.getCategoryId())
					.orElse(new PropertyCategory(1l,"Delux"));
		},executorService);

		CompletableFuture<Country> countryFuture = CompletableFuture.supplyAsync(()->{
			return countryRepository
					.findById(propertyDetailDto.getCountryId())
					.orElseThrow(()-> new RuntimeException("Country is Invalid"));
		},executorService);

		CompletableFuture<City> cityFuture = CompletableFuture.supplyAsync(()->{
			return cityRepository
					.findById(propertyDetailDto.getCityId())
					.orElseThrow(()-> new RuntimeException("City is Invalid"));
		},executorService);

		CompletableFuture<State> stateFuture = CompletableFuture.supplyAsync(()->{
			return stateRepository
					.findById(propertyDetailDto.getStateId())
					.orElseThrow(()-> new RuntimeException("State is Invalid"));
		},executorService);

		CompletableFuture<Location> locationFuture = CompletableFuture.supplyAsync(()->{
			return locationRepository
					.findById(propertyDetailDto.getStateId())
					.orElseThrow(()-> new RuntimeException("Location is Invalid"));
		},executorService);

		CompletableFuture.allOf(propertyCategoryFuture,countryFuture,stateFuture,cityFuture,locationFuture).join();

		PropertyCategory propertyCategory = null;
		Country country = null;
		State state =null;
		Location location = null;
		City city = null;

		try {
			propertyCategory = propertyCategoryFuture.get();
			country = countryFuture.get();
			state = stateFuture.get();
			city = cityFuture.get();
			location = locationFuture.get();
		}catch (ExecutionException | InterruptedException e){
			log.error(e.getMessage());
		}finally{
			executorService.shutdown();
		}

		if(propertyCategory != null && country != null && state != null && city != null && location != null){
			property.setPropertyCategory(propertyCategory);
			property.setCountry(country);
			property.setState(state);
			property.setCity(city);
			property.setLocation(location);
			property.setUserId(propertyDetailDto.getProperty().getUserId());
			return propertyRepository.save(property);
		}else {
			return null;
		}
	}

	@Override
	public String deleteProperty(String propertyId) {
		Optional<Property> byId = propertyRepository.findById(propertyId);
		if(byId.isPresent()) {
			Property property = byId.get();
			propertyRepository.delete(property);
			Optional<Property> byIdCheck = propertyRepository.findById(property.getPropertyId());
			if(byIdCheck.isEmpty()) {
				return "Deleted";
			}
		}
		return "Property Is Not Found";
	}

	@Override
	public Property updateProperty(String propertyId, Property updatedProperty) {
		Optional<Property> byId = propertyRepository.findById(propertyId);
		if(byId.isPresent()) {
			Property property = byId.get();
			property.setIsPetAllowed(updatedProperty.getIsPetAllowed());
			property.setIsBookingStarted(updatedProperty.getIsBookingStarted());
			property.setNoOfBathrooms(updatedProperty.getNoOfBathrooms());
			property.setNoOfRooms(updatedProperty.getNoOfRooms());
			property.setNoOfGuest(updatedProperty.getNoOfGuest());
			property.setNoOfBeds(updatedProperty.getNoOfBeds());
			property.setPropertyName(updatedProperty.getPropertyName());
			return propertyRepository.save(property);
		}
		return null;
	}

	@Override
	public List<Property> allPropertyByOwnerUserId(String ownerUserId) {
		List<Property> properties = propertyRepository.findByUserId(ownerUserId);
		return properties;
	}

	@Override
	public List<Property> searchProperty(SearchDto searchDto) {
		Sort sort = Sort.by(Direction.DESC, "viewCount");
		PageRequest request = PageRequest.of(searchDto.getPageNo()-1, searchDto.getPageSize() , sort);
		Page<Property> page = propertyRepository.searchPropertyByQUery(searchDto.getSearchQuery() , request);
		return page.getContent();
	}

	@Override
	public Property getPropertyById(String propertyId) {
		Optional<Property> byId = propertyRepository.findById(propertyId);
		if(byId.isPresent()) {
			Property property = byId.get();
			property.setViewCount(property.getViewCount()+1);
			return propertyRepository.save(property);
		}
		return null;
	}

	@Override
	public Boolean updatePropertyClick(String propertyId) {
		Property propertyById = getPropertyById(propertyId);
		return propertyById != null;
	}
	
}
