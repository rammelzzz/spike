package com.imooc.spike.controller;

import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeOrder;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.service.GoodsService;
import com.imooc.spike.service.OrderService;
import com.imooc.spike.service.SpikeService;
import com.imooc.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午12:42 18-4-18
 * @Modified By:
 **/
@Controller
@RequestMapping("/spike")
public class SpikeController {

        @Autowired
        private GoodsService goodsService;
        @Autowired
        private OrderService orderService;
        @Autowired
        private SpikeService spikeService;

        //在高并发情况下可能会把库存减为负数
        @RequestMapping(value = "/do_spike", method = RequestMethod.POST)
        public String spike(Model model, SpikeUser user,
                            @RequestParam("goodsId") long goodsId) {
                if (user == null) {
                        return "login";
                }
                model.addAttribute("user", user);
                GoodsVo goods = goodsService.getById(goodsId);
                int stock = goods.getGoodsStock();
                if (stock <= 0) {
                        model.addAttribute("error", CodeMsg.SPIKE_OVER.getMsg());
                        return "miaosha_fail";
                }
                //判断是不是已经秒杀过
                SpikeOrder order = orderService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);
                if (order != null) {
                        model.addAttribute("error", CodeMsg.REPEAT_SPIKE);
                        return "miaosha_fail";
                }
                //减库存 下订单 写入秒杀订单
                OrderInfo orderInfo = spikeService.spike(user, goods);
                model.addAttribute("orderInfo", orderInfo);
                model.addAttribute("goods", goods);
                return "order_detail";
        }

}
