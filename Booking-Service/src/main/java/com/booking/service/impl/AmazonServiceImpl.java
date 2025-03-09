package com.booking.service.impl;

import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.booking.service.AmazonService;

@Service
public class AmazonServiceImpl implements AmazonService {
	
	private final AmazonS3 amazons3;
	
	public AmazonServiceImpl(AmazonS3 amazons3) {
		this.amazons3 = amazons3;
	}

	@Value("${aws.bucket-name}")
	private String bucketName;

	@Override
	public Boolean deletePdf(String key) {
		amazons3.deleteObject(bucketName, key);
		URL url = amazons3.getUrl(bucketName, key);
		System.out.println(url);
		if(url.toString() == null) {
			return true;
		}
		return false;
	}

}
