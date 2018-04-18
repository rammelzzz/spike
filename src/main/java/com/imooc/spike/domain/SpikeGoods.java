package com.imooc.spike.domain;

import java.util.Date;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 上午10:49 18-4-18
 * @Modified By:
 **/
public class SpikeGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double spikePrice;

    public Double getSpikePrice() {
        return spikePrice;
    }

    public void setSpikePrice(Double spikePrice) {
        this.spikePrice = spikePrice;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public Integer getStockCount() {
        return stockCount;
    }
    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
