package com.imooc.spike.service;

import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeOrder;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.mapper.GoodsMapper;
import com.imooc.spike.mapper.OrderMapper;
import com.imooc.spike.redis.OrderKey;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午6:51 18-4-18
 * @Modified By:
 **/
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisService redisService;

    public SpikeOrder getSpikeOrderByUserIdGoodsId(long userId, long goodsId) {
//        return orderMapper.getSpikeOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getSpikeOrderByUidGid,
                "" + userId + ":" + goodsId,
                SpikeOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(SpikeUser user, GoodsVo goods) {
        //插入訂單
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSpikePrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insertOrderInfo(orderInfo);
        //插入秒殺訂單
        SpikeOrder spikeOrder = new SpikeOrder();
        spikeOrder.setGoodsId(goods.getId());
        spikeOrder.setOrderId(orderInfo.getId());
        spikeOrder.setUserId(user.getId());
        orderMapper.insertSpikeOrder(spikeOrder);
        //訂單寫入Redis
        redisService.set(OrderKey.getSpikeOrderByUidGid, "" + user.getId() + ":" + goods.getId(), spikeOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderMapper.getOrderById(orderId);
    }
}
