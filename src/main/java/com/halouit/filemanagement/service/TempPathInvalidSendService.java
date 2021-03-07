package com.halouit.filemanagement.service;

import com.halouit.filemanagement.enums.QueueEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TempPathInvalidSendService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Long id){
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_TEMP_TTL_INVALID.getExchange()
                ,QueueEnum.QUEUE_TEMP_TTL_INVALID.getRouteKey(),id,message -> {
                    message.getMessageProperties().setExpiration(String.valueOf(60 * 1000));
                    return message;
                });
    }


}
