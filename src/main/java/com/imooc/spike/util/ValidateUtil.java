package com.imooc.spike.util;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午9:55 18-4-17
 * @Modified By:
 **/
public class ValidateUtil {


        private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

        public static boolean isMobile(String src) {
                if (StringUtils.isEmpty(src)) {
                        return false;
                }
                Matcher m = mobile_pattern.matcher(src);
                return m.matches();
        }

        public static void main(String[] args) {
                System.out.println(isMobile("1881136295"));
        }

}
