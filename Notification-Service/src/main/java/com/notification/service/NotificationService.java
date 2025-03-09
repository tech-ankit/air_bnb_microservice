package com.notification.service;

import com.notification.payload.MessageDto;
import com.notification.payload.OtpMessageDto;

public interface NotificationService {
    void sendSms(MessageDto messageDto);
    void sendWhatsappMessageWithMedia(com.booking.payload.MessageDto messageDto);
    void sendPlainWhatsappMessage(MessageDto messageDto);
    void sendWhatsappOtp(OtpMessageDto otpMessageDto);
    void sendSmsOtp(OtpMessageDto otpMessageDto);
}
