package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午2:46 18-4-21
 * @Modified By:
 **/
public class SpikeGoodsKey extends BasePrefix {

    public SpikeGoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SpikeGoodsKey isGoodsOver = new SpikeGoodsKey(0, "igo");
    public static SpikeGoodsKey getSpikePath = new SpikeGoodsKey(60, "gsp");
}
