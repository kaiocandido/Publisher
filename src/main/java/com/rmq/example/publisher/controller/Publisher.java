package com.rmq.example.publisher.controller;

import com.rmq.example.publisher.service.PubliserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Publisher {

    @Value("${rabbitmq.qeuename}")
    private  String qeueName;

    @Autowired
    private PubliserService publiserService;

    @PostMapping("/publisher/Text")
    public void publisherText(@RequestBody String text){
        System.out.println("Sending message: " + text);
        publiserService.publishTextMessage(text, qeueName);

    }
}
