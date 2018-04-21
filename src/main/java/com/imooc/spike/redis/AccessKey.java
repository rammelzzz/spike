package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午8:24 18-4-21
 * @Modified By:
 **/
public class AccessKey extends BasePrefix {
    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey access = new AccessKey(5, "access");
    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }
}
