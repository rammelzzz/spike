package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午7:39 18-4-19
 * @Modified By:
 **/
public class GoodsKey extends BasePrefix {



    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
    public static GoodsKey getSpikeGoodsStock = new GoodsKey(0, "gsgs");

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
