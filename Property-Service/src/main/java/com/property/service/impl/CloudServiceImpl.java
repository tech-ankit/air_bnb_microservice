package com.property.service.impl;

import java.io.File;

import org.springdoc.core.service.RequestBodyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.property.service.CloudService;

@Service
public class CloudServiceImpl implements CloudService {
	
	@Value("${aws.bucket-name}")
	private String bucketName;
	
	private final AmazonS3 amazons3;
	
	public CloudServiceImpl(AmazonS3 amazons3) {
		this.amazons3 = amazons3;
	}

	@Override
	public String uploadImage(File file ,String key) {
		amazons3.putObject(bucketName, key, file);
		String url = amazons3.getUrl(bucketName, key).toString();
		return url;
	}
	
	@Override
	public Boolean deleteImage(String url) {
		String key = extractKeyFromUrl(url);
		if (amazons3.doesObjectExist(bucketName, key)) {
		    amazons3.deleteObject(bucketName, key);
		    try {
		    	url = amazons3.getObjectAsString(bucketName, key);
		    	if(url != null) {
		    	    return false;
		    	}
		    }catch(Exception e) {
		    	return true;
		    }
		}
         return url == null;
	}

	private String extractKeyFromUrl(String url) {
		int lastIndexOf = url.lastIndexOf("/");
		return url.substring(lastIndexOf+1);
	}

}
