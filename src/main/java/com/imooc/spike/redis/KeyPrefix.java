package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午7:18 18-4-17
 * @Modified By:
 **/
public interface KeyPrefix {

        public int expireSeconds();

        public String getPrefix();

}
