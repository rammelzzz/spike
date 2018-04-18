package com.imooc.spike.mapper;

import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeOrder;
import org.apache.ibatis.annotations.*;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午7:00 18-4-18
 * @Modified By:
 **/
@Mapper
public interface OrderMapper {

        @Select("select * from spike_order where user_id = #{userId} and goods_id = #{goodsId}")
        SpikeOrder getSpikeOrderByUserIdGoodsId(@Param("userId") long userId,
                                                @Param("goodsId") long goodsId);

        @Insert({"insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price," +
                "order_channel, status, create_date) " +
                "values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})"})
        @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
        long insertOrderInfo(OrderInfo orderInfo);

        @Insert("insert into spike_order(user_id, goods_id, order_Id)" +
                "values(#{userId}, #{goodsId}, #{orderId})")
        void insertSpikeOrder(SpikeOrder spikeOrder);
}
