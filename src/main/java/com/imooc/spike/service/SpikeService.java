package com.imooc.spike.service;

import com.imooc.spike.domain.Goods;
import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.mapper.GoodsMapper;
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
            goodsService.reduceStock(goods);
            //下订单 写入秒杀订单
            return orderService.createOrder(user, goods);
        }

}
