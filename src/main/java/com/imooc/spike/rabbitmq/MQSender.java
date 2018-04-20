package com.imooc.spike.rabbitmq;

import com.imooc.spike.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午5:03 18-4-20
 * @Modified By:
 **/
@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(Object message) {
        String msg = RedisService.beanToStr(message);
        log.info("send msg = {}", msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }

    public void sendTopic(Object object) {
        String msg = RedisService.beanToStr(object);
        log.info("send topic message = {}", msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key.1",  msg + "1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key.2",  msg + "2");
    }

    public void sendFanout(Object object) {
        String msg = RedisService.beanToStr(object);
        log.info("send fanout message = {}", msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key.1",  msg + "1");
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "",  msg + "2");
    }

    public void sendHeaders(Object object) {
        String msg = RedisService.beanToStr(object);
        log.info("send headers message = {}", msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message message = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", message);
    }

}
