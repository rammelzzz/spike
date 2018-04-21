package com.imooc.spike.controller;

import com.imooc.spike.access.AccessLimit;
import com.imooc.spike.domain.OrderInfo;
import com.imooc.spike.domain.SpikeOrder;
import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.rabbitmq.MQSender;
import com.imooc.spike.rabbitmq.SpikeMessage;
import com.imooc.spike.redis.AccessKey;
import com.imooc.spike.redis.GoodsKey;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.redis.SpikeGoodsKey;
import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.result.Result;
import com.imooc.spike.service.GoodsService;
import com.imooc.spike.service.OrderService;
import com.imooc.spike.service.SpikeService;
import com.imooc.spike.util.MD5Util;
import com.imooc.spike.util.UUIDUtil;
import com.imooc.spike.vo.GoodsVo;
import com.rabbitmq.client.AMQP;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午12:42 18-4-18
 * @Modified By:
 **/
@Controller
@RequestMapping("/spike")
public class SpikeController implements InitializingBean{

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SpikeService spikeService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender sender;
    private static final Logger log = LoggerFactory.getLogger(SpikeController.class);
    //用並發安全的ConcurrentHashMap進行內存標記，減少Redis的訪問次數
    //標記內容爲某個商品id的商品是否賣完
    private Map<Long, Boolean> localOverMap = new ConcurrentHashMap<>();
//    //在高并发情况下可能会把库存减为负数
//    @RequestMapping(value = "/do_spike", method = RequestMethod.POST)
//    public String spike(Model model, SpikeUser user,
//                        @RequestParam("goodsId") long goodsId) {
//        if (user == null) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//        GoodsVo goods = goodsService.getById(goodsId);
//        int stock = goods.getGoodsStock();
//        if (stock <= 0) {
//            model.addAttribute("error", CodeMsg.SPIKE_OVER.getMsg());
//            return "miaosha_fail";
//        }
//        //判断是不是已经秒杀过
//        SpikeOrder order = orderService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (order != null) {
//            model.addAttribute("error", CodeMsg.REPEAT_SPIKE);
//            return "miaosha_fail";
//        }
//        //减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = spikeService.spike(user, goods);
//        model.addAttribute("orderInfo", orderInfo);
//        model.addAttribute("goods", goods);
//        return "order_detail";
//    }

    //在高并发情况下可能会把库存减为负数

    /**
     * 重构代码，前后端分离，提高qps
     * get post的区别
     * get幂等 <a href="/delete?id=xxx"></a> 这里就是严重的错误
     * post提交变化
     * <pre>同步下單->異步下單</pre>
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 3)
    @RequestMapping(value = "/{path}/do_spike", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> spike(SpikeUser user,
                                 @RequestParam("goodsId") long goodsId,
                                 @PathVariable("path") String path) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        //驗證path
        boolean check = spikeService.checkPath(user, goodsId, path);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        /*
        //判斷庫存
        GoodsVo goods = goodsService.getById(goodsId);
        int stock = goods.getGoodsStock();
        if (stock <= 0) {
            return Result.error(CodeMsg.SPIKE_OVER);
        }
        //判断是不是已经秒杀过
        SpikeOrder order = orderService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SPIKE);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = spikeService.spike(user, goods);
        return Result.success(orderInfo);
         */


        //做內存標記,減少訪問Redis的次數
        boolean isOver = localOverMap.get(goodsId);
        if(isOver) {
            return Result.error(CodeMsg.SPIKE_OVER);
        }

        //判断是不是已经秒杀过
        //這裏已經優化過，從Redis中拿到這個秒殺商品對象
        SpikeOrder order = orderService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);//n個請求之後就會一直減成負數
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SPIKE);
        }

        //Redis優化秒殺
        long stock = redisService.decr(GoodsKey.getSpikeGoodsStock, "" + goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SPIKE_OVER);
        }
        //RabbitMQ異步下單
        SpikeMessage message = new SpikeMessage();
        message.setGoodsId(goodsId);
        message.setUser(user);
        sender.sendSpikeMessage(message);
        return Result.success(0); //0代表排隊中
    }

    /**
     * 繼承這個方法用以實現系統初始化時加載秒殺數據到Redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for(GoodsVo goods : goodsList) {
            //緩存庫存數據
            log.info("goodsId={},stock={}", goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
            redisService.set(GoodsKey.getSpikeGoodsStock, "" + goods.getId(), goods.getGoodsStock());
        }
    }

    /**
     * orderId: 成功
     * -1 : 秒殺失敗
     * 0 : 排隊中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> spikeResult(Model model, SpikeUser user,
                                       @RequestParam("goodsId") long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        long result = spikeService.getSpikeResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSpikePath(Model model, SpikeUser user,
                                    @RequestParam("goodsId") long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        String str = spikeService.createPath(user, goodsId);
        return Result.success(str);
    }
}
