package com.booking.service;

import com.booking.payload.MessageDto;

public interface KafkaMessageProducer {
	
	void sendMessage(MessageDto messageDto);

}
