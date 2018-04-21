package com.imooc.spike.access;

import com.imooc.spike.domain.SpikeUser;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午9:46 18-4-21
 * @Modified By:
 **/
public class UserContext {

    //用ThreadLocal来保存用户对象，在同一个线程下可以取到保存的那个User对象
    private static ThreadLocal<SpikeUser> userHolder = new ThreadLocal<>();

    public static void setUser(SpikeUser user) {
        userHolder.set(user);
    }

    public static SpikeUser getUser() {
        return userHolder.get();
    }

}
