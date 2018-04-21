package com.imooc.spike.access;

import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.redis.AccessKey;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.result.Result;
import com.imooc.spike.service.SpikeUserService;
import com.imooc.spike.service.UserService;
import com.imooc.spike.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: rammelzzz
 * @Description: 拦截器，拦截未登录的，访问流量过大的请求
 * @Date: Created in 下午9:38 18-4-21
 * @Modified By:
 **/
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SpikeUserService userService;
    @Autowired
    private RedisService redisService;
    private static final Logger log = LoggerFactory.getLogger(AccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            //将user保存在ThreadLocal中
            SpikeUser user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit access = hm.getMethodAnnotation(AccessLimit.class);
            if(access == null) {
                return true;
            }

            int seconds = access.seconds();
            int maxCount = access.maxCount();
            boolean needLogin = access.needLogin();
            String key = request.getRequestURI();
            int index = key.lastIndexOf("/");
            key = key.substring(index + 1);

            if(needLogin) {
                if(user == null) {
                    WebUtil.render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
                //接口限流防刷
                AccessKey accessKey = AccessKey.withExpire(seconds);
                Integer count = redisService.get(accessKey, key, Integer.class);
                log.info(accessKey.getPrefix() + ":" + key);
                log.info("count={}",count);
                if(count == null) {
                    redisService.set(accessKey, key, 1);
                } else if(count < maxCount) {
                    redisService.incr(accessKey, key);
                } else {
                    WebUtil.render(response, CodeMsg.ACCESS_LIMIT);
                    return false;
                }
            }
        }
        return true;
    }

    private SpikeUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(SpikeUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, SpikeUserService.COOKIE_NAME_TOKEN);

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
