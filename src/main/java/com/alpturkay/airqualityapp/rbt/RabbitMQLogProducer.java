package com.alpturkay.airqualityapp.rbt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQLogProducer {

    private final AmqpTemplate amqpTemplate;

    @Value("${airquality.rabbitmq.routingkey}")
    private String routingkey;

    @Value("${airquality.rabbitmq.exchange}")
    private String exchange;


    public void produce(LogData logData){
        amqpTemplate.convertAndSend(exchange, routingkey, logData);
        log.info("Message Sent: {}", logData.getLogData());
    }
}
