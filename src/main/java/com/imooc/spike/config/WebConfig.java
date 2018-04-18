package com.imooc.spike.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 上午10:29 18-4-18
 * @Modified By:
 **/
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {


        @Autowired
        private SpikeUserArgumentResolver userArgumentResolver;

        //添加一个user的解析器,如果参数列表中有user则传入
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(userArgumentResolver);
        }
}
