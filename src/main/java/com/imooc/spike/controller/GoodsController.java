package com.imooc.spike.controller;

import com.imooc.spike.domain.Goods;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.service.GoodsService;
import com.imooc.spike.service.SpikeUserService;
import com.imooc.spike.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 上午10:08 18-4-18
 * @Modified By:
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private SpikeUserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    //重构代码，改变由cookie获取user信息的方式,详见com.imooc.spike.config包下的内容
    @RequestMapping("/to_list")
    public String toList(Model model,
//                         @CookieValue(value = SpikeUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//                         @RequestParam(value = SpikeUserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                            SpikeUser user) {
//        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        SpikeUser user = userService.getByToken(response, token);

        //查询商品表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("user", user);
        return "goods_list";
    }


    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, SpikeUser user,
                         @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getById(goodsId);
        model.addAttribute("goods", goods);

        //判断秒杀是否开启
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int spikeStatus = -1;
        int remainSeconds = 0;

        if(now < startAt) { //秒杀没有开启，倒计时
            spikeStatus = 0;
            remainSeconds = (int)((startAt - now) /1000);
        } else if (now > endAt) { //秒杀已经结束
            spikeStatus = 2;
            remainSeconds = -1;
        } else {  //秒杀进行中
            spikeStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("spikeStatus", spikeStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
