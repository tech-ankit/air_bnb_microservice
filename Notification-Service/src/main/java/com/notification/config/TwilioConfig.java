package com.notification.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.whatsapp-number}")
    private String whatsappNumber;

    @Value("${twilio.phone-number}")
    private String phoneNumber;

    @PostConstruct
    public void twilioInitializer() {
        Twilio.init(accountSid, authToken);
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getWhatsappNumber(){
        return whatsappNumber;
    }
}
