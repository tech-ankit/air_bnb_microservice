package com.property.service.impl;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.property.entity.Property;
import com.property.entity.PropertyImage;
import com.property.entity.PropertyImageCategory;
import com.property.payload.PropertyImageRequest;
import com.property.repository.PropertyImageCategoryRepository;
import com.property.repository.PropertyImageRepository;
import com.property.repository.PropertyRepository;
import com.property.service.AWSService;
import com.property.service.CloudService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AWSServiceImpl implements AWSService {
	
	private final PropertyImageCategoryRepository propertyImageCategoryRepository;
	private final PropertyImageRepository propertyImageRepository;
	private final PropertyRepository propertyRepository;
	private final CloudService cloudService;

	@Override
	public Boolean addPropertyImage(PropertyImageRequest propertyImageRequest) {
		PropertyImageCategory category = propertyImageCategoryRepository
				.findById(1l)
				.orElse(new PropertyImageCategory(2L,"Hotel"));
		Optional<Property> byId = propertyRepository.findById(propertyImageRequest.getPropertyId());
		if(byId.isPresent()) {
			PropertyImage propertyImage = new PropertyImage();
			propertyImage.setImageDescription(propertyImageRequest.getDescription());
			propertyImage.setImageTitle(propertyImageRequest.getTitle());
			List<MultipartFile> images = propertyImageRequest.getImages();
			List<String> imageUrls = new ArrayList<>();
			List<String> imageKeys = new ArrayList<>();
			for(MultipartFile image:images) {
				String uniqueFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
				File tempFile = null;
				String prefix = "air-bnb-image";
				try {
					tempFile = File
							.createTempFile(prefix,uniqueFileName);
					image.transferTo(tempFile);
					String key = prefix+uniqueFileName;
					String url = cloudService.uploadImage(tempFile,key);
					imageUrls.add(url);
					imageKeys.add(key);
				}catch(Exception e) {
					log.error(e.getMessage());
				}finally {
					tempFile.deleteOnExit();
				}
			}
			propertyImage.setImageUrl(concatStringByComa(imageUrls));
			propertyImage.setImageKey(concatStringByComa(imageKeys));
			propertyImage.setPropertyImageCategory(category);
			propertyImage.setProperty(byId.get());
			PropertyImage image = propertyImageRepository.save(propertyImage);
			if(image.getPropertyImageId() != null) {
				return true ;
			}
		}
		return false;
	}

	@Override
	public Boolean deleteImage(String imageUrl,Long propertyImageId) {
		Optional<PropertyImage> byId = propertyImageRepository.findById(propertyImageId);
		if(byId.isPresent()) {
			PropertyImage propertyImage = byId.get();
			if(propertyImage.getImageUrl()!=null) {
				String key = propertyImage.getImageUrl();
				String updatedUrls = Arrays.stream(key.split(",")) 
	                    .map(String::trim)
	                    .filter(img -> !img.equals(imageUrl))
	                    .collect(Collectors.joining(","));
				propertyImage.setImageUrl(updatedUrls);
				Boolean isDeleted = cloudService.deleteImage(imageUrl);
				if(isDeleted) {
					propertyImageRepository.save(propertyImage);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String updateImage(Long propertyImageId, String propertyId, PropertyImage propertyImage) {
		// TODO UPDATE_IMAGE
		return null;
	}

	@Override
	public List<String> getImagesByProperty(String propertyId) {
		Optional<Property> byId = propertyRepository.findById(propertyId);
		if(byId.isPresent()) {
			Optional<PropertyImage> opPropertyImage = propertyImageRepository.findByProperty(byId.get());
			if(opPropertyImage.isPresent()) {
				PropertyImage propertyImage = opPropertyImage.get();
				String url = propertyImage.getImageUrl();
				return Arrays.stream(url.split(",")).map(String::trim).collect(Collectors.toList());
			}
		}
		return null;
	}
	
	private String concatStringByComa(List<String> imageUrls) {
		return imageUrls.stream().map(str->str.trim()).collect(Collectors.joining(","));
	}

}
