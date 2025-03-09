package com.property.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface CloudService {
	
	String uploadImage(File file,String key);
	Boolean deleteImage(String url);

}
