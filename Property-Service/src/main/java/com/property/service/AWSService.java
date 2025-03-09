package com.property.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.property.entity.PropertyImage;
import com.property.payload.PropertyImageRequest;

public interface AWSService  {
	Boolean addPropertyImage(PropertyImageRequest propertyImageRequest);
    Boolean deleteImage(String url,Long propertyImageId);
	String updateImage(Long propertyImageId , String propertyId ,PropertyImage propertyImage);
	List<String> getImagesByProperty(String propertyId);
}
