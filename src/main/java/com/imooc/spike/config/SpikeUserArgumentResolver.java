package com.imooc.spike.config;

import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.service.SpikeUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 上午10:32 18-4-18
 * @Modified By:
 **/
@Service
public class SpikeUserArgumentResolver implements HandlerMethodArgumentResolver {

        @Autowired
        private SpikeUserService userService;

        @Override
        public boolean supportsParameter(MethodParameter methodParameter) {
                Class<?> clz = methodParameter.getParameterType();
                return SpikeUser.class == clz;
        }

        /**
         * 如果参数列表中含有SpikeUser,那么使用这个装配器来装配它
         *
         * @param methodParameter
         * @param modelAndViewContainer
         * @param nativeWebRequest
         * @param webDataBinderFactory
         * @return
         * @throws Exception
         */
        @Override
        public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
                HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
                HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

                String paramToken = request.getParameter(SpikeUserService.COOKIE_NAME_TOKEN);
                String cookieToken = getCookieValue(request, SpikeUserService.COOKIE_NAME_TOKEN);

                if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
                        return "login";
                }
                String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
                return userService.getByToken(response, token);
        }

        public String getCookieValue(HttpServletRequest request, String cookieName) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(cookieName)) {
                                return cookie.getValue();
                        }
                }
                return null;
        }
}
