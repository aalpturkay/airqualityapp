package com.alpturkay.airqualityapp.rbt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logSender")
public class RabbitLogController {

    private final RabbitMQLogProducer rabbitMQLogProducer;

    @PostMapping
    public void sendLog(@RequestBody LogData logData){
        rabbitMQLogProducer.produce(logData);
    }
}
