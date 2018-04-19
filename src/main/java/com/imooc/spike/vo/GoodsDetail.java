package com.imooc.spike.vo;

import com.imooc.spike.domain.SpikeUser;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午8:31 18-4-19
 * @Modified By:
 **/
public class GoodsDetail {

    private int spikeStatus;
    private int remainSeconds;
    private GoodsVo goods;
    private SpikeUser user;

    public SpikeUser getUser() {
        return user;
    }

    public void setUser(SpikeUser user) {
        this.user = user;
    }

    public int getSpikeStatus() {
        return spikeStatus;
    }

    public void setSpikeStatus(int spikeStatus) {
        this.spikeStatus = spikeStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
}
