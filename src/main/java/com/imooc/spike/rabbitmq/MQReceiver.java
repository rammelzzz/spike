package com.imooc.spike.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午5:03 18-4-20
 * @Modified By:
 **/
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * Direct模式，交換機Exchange
     * @param message
     */
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message = {}", message);
    }

    /**
     * topic模式
     * @param message
     */
    @RabbitListener(queues = MQConfig.TOP_QUEUE_1)
    public void receiveTopic1(String message) {
        log.info("topic queue1 receive message = {}", message);
    }

    /**
     * topic模式
     * @param message
     */
    @RabbitListener(queues = MQConfig.TOP_QUEUE_2)
    public void receiveTopic2(String message) {
        log.info("topic queue2 receive message = {}", message);
    }

    /**
     * topic模式
     * @param message
     */
    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveHeaders(byte[] message) {
        log.info("headers queue receive message = {}", new String(message));
    }

}
