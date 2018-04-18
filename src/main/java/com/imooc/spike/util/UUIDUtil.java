package com.imooc.spike.util;

import java.util.UUID;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 上午10:01 18-4-18
 * @Modified By:
 **/
public class UUIDUtil {
        public static String uuid() {
                return UUID.randomUUID().toString().replace("-", "");
        }
}
