package com.imooc.spike.controller;

import com.imooc.spike.domain.Goods;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.redis.GoodsKey;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.result.Result;
import com.imooc.spike.service.GoodsService;
import com.imooc.spike.service.SpikeUserService;
import com.imooc.spike.vo.GoodsDetail;
import com.imooc.spike.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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

//    @Autowired
//    private SpikeUserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;

    //重构代码，改变由cookie获取user信息的方式,详见com.imooc.spike.config包下的内容
    /**
     * 第二次重构代码，添加页面缓存
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/to_list")
    @ResponseBody
    public String toList(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response,
//                         @CookieValue(value = SpikeUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//                         @RequestParam(value = SpikeUserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                         SpikeUser user) {
//        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        SpikeUser user = userService.getByToken(response, token);

        //返回缓存 begin
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        //返回缓存 end

        //查询商品表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("user", user);
//        return "goods_list";

        /* 第二次重构 */
        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request,
                response,
                request.getServletContext(),
                request.getLocale(),
                model.asMap(),
                applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }


//    /**
//     * 重构代码，加入页面缓存
//     * @param model
//     * @param user
//     * @param goodsId
//     * @return
//     */
//    @RequestMapping("/to_detail2/{goodsId}")
//    @ResponseBody
//    public String detail2(Model model, SpikeUser user,
//                         HttpServletRequest request,
//                         HttpServletResponse response,
//                         @PathVariable("goodsId") long goodsId) {
//
//        //返回缓存 begin
//        String html = redisService.get(GoodsKey.getGoodsList, String.valueOf(goodsId), String.class);
//        if(!StringUtils.isEmpty(html)) {
//            return html;
//        }
//        //缓存 end
//        model.addAttribute("user", user);
//        GoodsVo goods = goodsService.getById(goodsId);
//        model.addAttribute("goods", goods);
//
//        //判断秒杀是否开启
//        long startAt = goods.getStartDate().getTime();
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//
//        int spikeStatus;
//        int remainSeconds;
//
//        if (now < startAt) { //秒杀没有开启，倒计时
//            spikeStatus = 0;
//            remainSeconds = (int) ((startAt - now) / 1000);
//        } else if (now > endAt) { //秒杀已经结束
//            spikeStatus = 2;
//            remainSeconds = -1;
//        } else {  //秒杀进行中
//            spikeStatus = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("spikeStatus", spikeStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
////        return "goods_detail";
//        //手动渲染
//        SpringWebContext ctx = new SpringWebContext(request,
//                response,
//                request.getServletContext(),
//                request.getLocale(),
//                model.asMap(),
//                applicationContext);
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
//        if(!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsDetail, String.valueOf(goodsId), html);
//        }
//        return html;
//    }

    /**
     * 第三次重构代码，前后端分离，页面静态化
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetail> detail(@PathVariable("goodsId") long goodsId,
                                      SpikeUser user) {

        GoodsVo goods = goodsService.getById(goodsId);

        //判断秒杀是否开启
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int spikeStatus;
        int remainSeconds;

        if (now < startAt) { //秒杀没有开启，倒计时
            spikeStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) { //秒杀已经结束
            spikeStatus = 2;
            remainSeconds = -1;
        } else {  //秒杀进行中
            spikeStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetail vo = new GoodsDetail();
        vo.setGoods(goods);
        vo.setSpikeStatus(spikeStatus);
        vo.setRemainSeconds(remainSeconds);
        vo.setUser(user);

        return Result.success(vo);

    }
}
