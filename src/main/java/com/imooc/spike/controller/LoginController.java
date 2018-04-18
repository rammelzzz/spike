package com.imooc.spike.controller;

import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.result.Result;
import com.imooc.spike.service.SpikeUserService;
import com.imooc.spike.util.ValidateUtil;
import com.imooc.spike.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午9:32 18-4-17
 * @Modified By:
 **/
@Controller
@RequestMapping("/login")
public class LoginController {

        private Logger log = LoggerFactory.getLogger(getClass());
        @Autowired
        private SpikeUserService spikeUserService;

        @RequestMapping(value = "/to_login", method = RequestMethod.GET)
        public String toLogin() {
                return "login";
        }

        @RequestMapping(value = "/do_login", method = RequestMethod.POST)
        @ResponseBody
        public Result doLogin(@Valid LoginVo loginVo, HttpServletResponse response) {
                log.info(loginVo.toString());
                //参数校验
//        String inputPass = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        if(StringUtils.isEmpty(inputPass)) {
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if(StringUtils.isEmpty(mobile)) {
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if(!ValidateUtil.isMobile(mobile)) {
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
                //出现的业务异常已经被GlobalExceptionHandler捕获并处理，因此这里不关心结果如何
                //因为如果代码能进行到下一步那么结果必然是true
                spikeUserService.login(response, loginVo);
                return Result.success(true);
        }

}
