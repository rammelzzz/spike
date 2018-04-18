package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午7:22 18-4-17
 * @Modified By:
 **/
public class OrderKey extends BasePrefix {

        public OrderKey(int expireSeconds, String prefix) {
                super(expireSeconds, prefix);
        }
}
