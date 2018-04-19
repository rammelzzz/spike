package com.imooc.spike.vo;

import com.imooc.spike.domain.OrderInfo;

/**
 * @Author: rammelzzz
 * @Description: order_detail页面所需vo
 * @Date: Created in 下午10:23 18-4-19
 * @Modified By:
 **/
public class OrderVo {

    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
