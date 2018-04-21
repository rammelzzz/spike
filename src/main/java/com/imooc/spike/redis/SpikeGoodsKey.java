package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午2:46 18-4-21
 * @Modified By:
 **/
public class SpikeGoodsKey extends BasePrefix {

    public SpikeGoodsKey(String prefix) {
        super(prefix);
    }

    public static SpikeGoodsKey isGoodsOver = new SpikeGoodsKey("igo");
}
