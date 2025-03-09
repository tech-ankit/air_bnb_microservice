package com.booking.rest;

import java.security.MessageDigest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.payload.MessageDto;
import com.booking.service.KafkaMessageProducer;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/notification")
@AllArgsConstructor
public class NotificationRestController {
	

	private final KafkaMessageProducer kafkaMessageProducer;
	
	@PostMapping(value = "/send")
	public String sendMessage(@RequestBody MessageDto messageDto) {
		kafkaMessageProducer.sendMessage(messageDto);
		return "Message Send Successfully";
	}
}
