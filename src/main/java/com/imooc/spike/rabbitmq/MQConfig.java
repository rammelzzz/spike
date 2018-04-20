package com.imooc.spike.rabbitmq;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午5:03 18-4-20
 * @Modified By:
 **/
@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    public static final String TOP_QUEUE_1 = "topic.queue.1";
    public static final String TOP_QUEUE_2 = "topic.queue.2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchange";
    public static final String HEADERS_QUEUE = "headersQueue";
    /**
     * Direct模式，交換機Exchange
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    /**
     * Topic模式，交換機Exchange
     * @return
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOP_QUEUE_1, true);
    }

    /**
     * Topic模式，交換機Exchange
     * @return
     */
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOP_QUEUE_2, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2() {
        //#代表0個或多個單詞
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }

    //Fanout 廣播模式，所有的queue都能收到
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headersQueue() {
        return new Queue(HEADERS_QUEUE);
    }

    @Bean
    public Binding headersBinding() {
        Map<String, Object> map = new HashMap<>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
    }
}
