package com.booking.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.booking.payload.MessageDto;
import com.booking.service.KafkaMessageProducer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KafkaMessageProducerImpl implements KafkaMessageProducer {

	private final KafkaTemplate<String,MessageDto> kafkaTemplate;
	
	@Override
	public void sendMessage(MessageDto messageDto) {
		kafkaTemplate.send("my-topic",messageDto);
	}

}
