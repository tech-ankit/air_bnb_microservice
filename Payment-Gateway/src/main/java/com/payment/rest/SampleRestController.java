package com.payment.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SampleRestController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping(value = "/kafka")
    public void send(@RequestParam String bookingNumber){
        kafkaTemplate.send("pdf-topic",bookingNumber);
    }

}
