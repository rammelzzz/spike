package com.imooc.spike.rabbitmq;

import com.imooc.spike.domain.SpikeUser;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午2:25 18-4-21
 * @Modified By:
 **/
public class SpikeMessage {

    private SpikeUser user;
    private long goodsId;

    public SpikeUser getUser() {
        return user;
    }

    public void setUser(SpikeUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
