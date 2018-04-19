package com.imooc.spike.controller;

import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.result.Result;
import com.imooc.spike.service.GoodsService;
import com.imooc.spike.service.OrderService;
import com.imooc.spike.vo.GoodsVo;
import com.imooc.spike.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:22 18-4-19
 * @Modified By:
 **/
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderVo> detail(SpikeUser user,
                                  @RequestParam("orderId") long orderId) {
        if(user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getById(goodsId);
        OrderVo vo = new OrderVo();
        vo.setGoods(goods);
        vo.setOrder(order);
        return Result.success(vo);
    }

}
