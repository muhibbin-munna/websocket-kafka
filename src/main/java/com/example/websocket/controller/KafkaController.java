package com.example.websocket.controller;

import com.example.websocket.dto.MessageRequest;
import com.example.websocket.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/publish")
    public String sendMessageToKafkaTopic(@RequestBody MessageRequest message) {
        String topic = "websocket-topic";
        kafkaProducerService.sendMessage(topic, message);
        return "Message sent to Kafka topic " + topic;
    }
}
