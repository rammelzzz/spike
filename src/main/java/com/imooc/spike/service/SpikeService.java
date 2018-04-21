package com.imooc.spike.service;

import com.imooc.spike.domain.Goods;
import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeOrder;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.mapper.GoodsMapper;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.redis.SpikeGoodsKey;
import com.imooc.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午6:56 18-4-18
 * @Modified By:
 **/
@Service
public class SpikeService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    /**
     * //减库存 下订单 写入秒杀订单
     *
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo spike(SpikeUser user, GoodsVo goods) {
        //减库存
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            //下订单 写入秒杀订单
            return orderService.createOrder(user, goods);
        }
        setGoodsOver(goods.getId());
        return null;
    }

    private void setGoodsOver(long goodsId) {
        redisService.set(SpikeGoodsKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exist(SpikeGoodsKey.isGoodsOver, "" + goodsId);
    }

    public long getSpikeResult(long id, long goodsId) {
        SpikeOrder order = orderService.getSpikeOrderByUserIdGoodsId(id, goodsId);
        if(order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
