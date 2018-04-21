package com.imooc.spike.rabbitmq;

import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeOrder;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.result.Result;
import com.imooc.spike.service.GoodsService;
import com.imooc.spike.service.OrderService;
import com.imooc.spike.service.SpikeService;
import com.imooc.spike.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpikeService spikeService;
    @Autowired
    private OrderService orderService;

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

    @RabbitListener(queues = MQConfig.SPIKE_QUEUE)
    public void receiveMessage(String msg) {
        log.info("receive message : {}", msg);
        SpikeMessage spikeMessage = RedisService.strToBean(msg, SpikeMessage.class);
        SpikeUser user = spikeMessage.getUser();
        long goodsId = spikeMessage.getGoodsId();
        //判斷庫存
        GoodsVo goods = goodsService.getById(goodsId);
        int stock = goods.getGoodsStock();
        if (stock <= 0) {
            return;
        }
        //判断是不是已经秒杀过
        SpikeOrder order = orderService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        spikeService.spike(user, goods);
    }

}
