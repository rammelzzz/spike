package com.imooc.spike.redis;


/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午7:22 18-4-17
 * @Modified By:
 **/
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
