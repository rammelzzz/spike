package com.imooc.spike.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午8:22 18-4-17
 * @Modified By:
 **/
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass) {
        String str = salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = salt.charAt(0) + salt.charAt(5) + formPass + salt.charAt(2) + salt.charAt(7);
        return md5(str);
    }

    public static String inputPassToDBPass(String input, String saltDB) {
        return formPassToDBPass(inputPassToFormPass(input), saltDB);
    }

    public static void main(String[] args) {
        System.out.println(formPassToDBPass("780a965c2eba425efdd2d9395bbebe97", "zclovecc"));
    }

}
