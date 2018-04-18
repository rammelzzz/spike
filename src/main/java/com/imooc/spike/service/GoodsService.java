package com.imooc.spike.service;

import com.imooc.spike.domain.SpikeGoods;
import com.imooc.spike.mapper.GoodsMapper;
import com.imooc.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午12:09 18-4-18
 * @Modified By:
 **/
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    public GoodsVo getById(long goodsId) {
        return goodsMapper.getById(goodsId);
    }

    @Transactional
    public void reduceStock(GoodsVo goods) {
        SpikeGoods g = new SpikeGoods();
        g.setGoodsId(goods.getId());
        goodsMapper.reduceStock(g);
    }
}
