package com.notification.rest;

import com.notification.payload.MessageDto;
import com.notification.payload.OtpMessageDto;
import com.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/notification")
public class NotificationRestController  {

    private final NotificationService notificationService;

    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/send-sms")
    public Boolean sendSms(@RequestBody MessageDto messageDto){
        notificationService.sendSms(messageDto);
        return true;
    }

    @PostMapping(value = "/send-whatsapp/media")
    public Boolean sendWhatsappWithMedia(@RequestBody com.booking.payload.MessageDto messageDto){
        notificationService.sendWhatsappMessageWithMedia(messageDto);
        return true;
    }

    @PostMapping(value = "/whatsapp/plain")
    public Boolean sendWhatsappPlain(@RequestBody MessageDto messageDto){
        notificationService.sendPlainWhatsappMessage(messageDto);
        return true;
    }

    @PostMapping(value = "/whatsapp/otp")
    public Boolean sendOtpWhatsapp(@RequestBody OtpMessageDto otpMessageDto){
        notificationService.sendWhatsappOtp(otpMessageDto);
        return true;
    }

    @PostMapping(value = "/sms/otp")
    public Boolean sendOtpSms(@RequestBody OtpMessageDto otpMessageDto){
        notificationService.sendSmsOtp(otpMessageDto);
        return true;
    }
}
