package com.notification.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sample")
public class SampleRestController {

    @GetMapping
    public String getSample(){
        return "Sample Secure Is Working";
    }
}
