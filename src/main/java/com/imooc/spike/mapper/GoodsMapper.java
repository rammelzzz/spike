package com.imooc.spike.mapper;

import com.imooc.spike.domain.Goods;
import com.imooc.spike.domain.SpikeGoods;
import com.imooc.spike.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午12:09 18-4-18
 * @Modified By:
 **/
@Mapper
public interface GoodsMapper {

        @Select("select g.*, mg.spike_price, mg.stock_count, mg.start_date, mg.end_date from spike_goods mg left join goods g on mg.goods_id = g.id")
        public List<GoodsVo> listGoodsVo();

        @Select("select g.*, mg.spike_price, mg.stock_count, mg.start_date, mg.end_date from spike_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
        public GoodsVo getById(@Param("goodsId") long goodsId);

        @Update("update spike_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
        public int reduceStock(SpikeGoods g);
}
