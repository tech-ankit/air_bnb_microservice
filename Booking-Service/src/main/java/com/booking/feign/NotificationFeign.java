package com.booking.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.payload.MessageDto;

@FeignClient(name = "NOTIFICATION-API")
public interface NotificationFeign {
	
	@PostMapping(value = "/api/v1/notification/send-sms")
    Boolean invokeSendSms(@RequestBody MessageDto messageDto);
	
	@PostMapping(value = "/api/v1/notification/send-whatsapp/media")
	Boolean invokeSendWhatsappWithMedia(@RequestBody MessageDto messageDto);

}
