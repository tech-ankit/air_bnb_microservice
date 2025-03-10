package com.notification.service.impl;

import com.notification.config.TwilioConfig;

import com.notification.payload.MessageDto;
import com.notification.payload.OtpMessageDto;
import com.notification.service.NotificationService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final TwilioConfig twilioConfig;

    public NotificationServiceImpl(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public void sendSms(MessageDto messageDto) {
        Message msg = Message.creator(
                new PhoneNumber(messageDto.getNumber().trim()),
                new PhoneNumber(twilioConfig.getPhoneNumber()),
                getMessageTemplateBooking(messageDto.getGuestName(), messageDto.getPropertyName())
        ).create();
        log.error("Error Message While Sending SMS Notification: {}",msg.getErrorMessage());
    }

    @Override
    @KafkaListener(topics = "notification-topic" , groupId = "message-group", containerFactory = "factory")
    public void sendWhatsappMessageWithMedia(com.booking.payload.MessageDto messageDto) {
        String whatsapp = "whatsapp:";
        Message msg = Message.creator(
                new PhoneNumber(whatsapp+messageDto.getNumber().trim()),
                new PhoneNumber(twilioConfig.getWhatsappNumber()),
                getMessageTemplateBookingWhatsapp(messageDto.getGuestName(),messageDto.getPropertyName())
        ).setMediaUrl(messageDto.getMediaUrl()).create();
        if(msg.getErrorMessage()!=null) {
        	log.error("Error Message While Sending Whatsapp Message: {}",msg.getErrorMessage());
        }else {
        	log.info("Message Send Successfully to :{}",messageDto.getGuestName());
        }
    }

    @Override
    public void sendPlainWhatsappMessage(MessageDto messageDto) {
        String whatsapp = "whatsapp:";
        Message msg = Message.creator(
                new PhoneNumber(whatsapp+messageDto.getNumber().trim()),
                new PhoneNumber(twilioConfig.getWhatsappNumber()),
                messageDto.getGuestName()
        ).create();
        log.error("Error Message While Sending Plain Whatsapp Message: {}",msg.getErrorMessage());
    }

    @Override
    public void sendWhatsappOtp(OtpMessageDto otpMessageDto) {
        String whatsapp = "whatsapp:";
        Message msg = Message.creator(
                new PhoneNumber(whatsapp+otpMessageDto.getNumber().trim()),
                new PhoneNumber(twilioConfig.getWhatsappNumber()),
                otpMessageDto.getMessage()
        ).create();
        log.error("Error While Sending Otp to Whatsapp: {}",msg.getErrorMessage());
    }

    @Override
    public void sendSmsOtp(OtpMessageDto otpMessageDto) {
        Message msg = Message.creator(
                new PhoneNumber(otpMessageDto.getNumber().trim()),
                new PhoneNumber(twilioConfig.getPhoneNumber()),
                otpMessageDto.getMessage()
        ).create();
        log.error("Error While Sending Otp to SMS: {}",msg.getErrorMessage());
    }
    
    private String getMessageTemplateBooking(String guestName,String propertyName) {
    	return String.format("""
    			Hello %s,
    			Welcome to Hotel %s,
    			Your Booking is Successful
    			""",guestName,propertyName);
    }
    
    private String getMessageTemplateBookingWhatsapp(String guestName,String propertyName) {
    	return String.format("""
    			Hello %s,
    			Welcome to Hotel %s,
    			Your Booking is Successful,
    			Your Invoice Pdf is Attached Below
    			""",guestName,propertyName);
    }
}
