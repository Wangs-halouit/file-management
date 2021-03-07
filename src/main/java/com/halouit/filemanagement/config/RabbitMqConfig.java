package com.halouit.filemanagement.config;

import com.halouit.filemanagement.enums.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public DirectExchange tempDirect(){
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_TEMP_INVALID.getExchange())
                .durable(true).build();
    }

    @Bean
    public DirectExchange tempTtlDirect() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_TEMP_TTL_INVALID.getExchange())
                .durable(true).build();
    }

    @Bean
    public Queue tempQueue(){
        return new Queue(QueueEnum.QUEUE_TEMP_INVALID.getName());
    }

    /**
     *死信队列
     */
    @Bean
    public Queue tempTtlQueue(){
        return QueueBuilder.durable(QueueEnum.QUEUE_TEMP_TTL_INVALID.getName())
                .deadLetterExchange(QueueEnum.QUEUE_TEMP_INVALID.getExchange())
                .deadLetterRoutingKey(QueueEnum.QUEUE_TEMP_INVALID.getRouteKey())
                .build();
    }

    @Bean
    public Binding orderBinging(DirectExchange tempDirect, Queue tempQueue){
        return BindingBuilder.bind(tempQueue)
                .to(tempDirect)
                .with(QueueEnum.QUEUE_TEMP_INVALID.getRouteKey());
    }

    @Bean
    public Binding orderTtlBinding(DirectExchange tempTtlDirect,Queue tempTtlQueue){
        return BindingBuilder.bind(tempTtlQueue)
                .to(tempTtlDirect)
                .with(QueueEnum.QUEUE_TEMP_TTL_INVALID.getRouteKey());
    }
}
