package com.imooc.spike.service;

import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeOrder;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.mapper.GoodsMapper;
import com.imooc.spike.mapper.OrderMapper;
import com.imooc.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
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

        public SpikeOrder getSpikeOrderByUserIdGoodsId(long userId, long goodsId) {
                return orderMapper.getSpikeOrderByUserIdGoodsId(userId, goodsId);
        }

        @Transactional
        public OrderInfo createOrder(SpikeUser user, GoodsVo goods) {
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
                long orderId = orderMapper.insertOrderInfo(orderInfo);
                SpikeOrder spikeOrder = new SpikeOrder();
                spikeOrder.setGoodsId(goods.getId());
                spikeOrder.setOrderId(orderId);
                spikeOrder.setUserId(user.getId());
                orderMapper.insertSpikeOrder(spikeOrder);
                return orderInfo;
        }
}
