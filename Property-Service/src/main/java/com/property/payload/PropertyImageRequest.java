package com.property.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyImageRequest {
	
	private String propertyId;
	private Long propertyImageCatogoryId;
	private List<MultipartFile> images;
	private String title;
	private String description;
}
