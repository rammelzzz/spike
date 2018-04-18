package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 上午10:14 18-4-18
 * @Modified By:
 **/
public class SpikeUserKey extends BasePrefix {

        private static final int TOKEN_EXPIRE = 3600 * 24 * 2;

        public SpikeUserKey(int expireSeconds, String prefix) {
                super(expireSeconds, prefix);
        }

        public static SpikeUserKey spikeUserKey = new SpikeUserKey(TOKEN_EXPIRE, "token");
}
