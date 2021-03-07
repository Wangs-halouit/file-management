package com.halouit.filemanagement.enums;

import lombok.Getter;

@Getter
public enum QueueEnum {
    /**
     * 消息通知队列
     */
    QUEUE_TEMP_INVALID("temp.direct","temp.invalid","temp.invalid"),
    /**
     * 消息通知ttl队列
     */
    QUEUE_TEMP_TTL_INVALID("temp.direct.ttl","temp.invalid.ttl","temp.invalid.ttl"),
    ;
    //交换机
    private String exchange;
    //队列名称
    private String name;
    //路由键
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
