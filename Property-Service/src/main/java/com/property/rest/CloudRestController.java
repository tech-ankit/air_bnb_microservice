package com.property.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.property.entity.PropertyImage;
import com.property.payload.PropertyImageRequest;
import com.property.service.AWSService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/cloud")
@AllArgsConstructor
public class CloudRestController  {
	
	private final AWSService awsService;
	
	@PostMapping(value = "/image-upload")
	public Boolean addPropertyImage(@RequestBody List<MultipartFile> files) {
		PropertyImageRequest property = new PropertyImageRequest();
		property.setImages(files);
		property.setDescription("Images of Room");
		property.setTitle("Room");
		property.setPropertyId("f18dce23-79ee-41f4-95b5-64a61dba0844");
		property.setPropertyImageCatogoryId(1L);
		return awsService.addPropertyImage(property);
	}
	
	@DeleteMapping(value = "/image/delete/{propertyImageId}")
	public Boolean deleteImage(@RequestParam String url,@PathVariable Long propertyImageId) {
		return awsService.deleteImage(url, propertyImageId);
	}
	
	@PutMapping(value = "/image/update/{propertyImageId}")
	public String updateImage(@PathVariable Long propertyImageId ,@RequestParam String propertyId ,@RequestBody PropertyImage propertyImage) {
		return awsService.updateImage(propertyImageId, propertyId, propertyImage);
	}
	
	@GetMapping(value = "/all-images")
	public List<String> getImagesByProperty(@RequestParam String propertyId){
		return awsService.getImagesByProperty(propertyId);
	}

}
