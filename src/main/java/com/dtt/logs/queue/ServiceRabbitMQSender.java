package com.dtt.logs.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;

@Service
public class ServiceRabbitMQSender {

    public void sendMessage(RabbitTemplate template, String exchange, Object data,
                            String key) {
        if(exchange==null && key!=null) template.convertAndSend(key, data);
        if(key==null && exchange!=null) template.convertAndSend(exchange,data);
        if(key==null && exchange==null) template.convertAndSend(data);
        else template.convertAndSend(  exchange, key, data);
    }


}
