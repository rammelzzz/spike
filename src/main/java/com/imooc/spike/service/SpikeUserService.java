package com.imooc.spike.service;

import com.imooc.spike.domain.SpikeUser;
import com.imooc.spike.exception.GlobalException;
import com.imooc.spike.mapper.SpikeUserMapper;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.redis.SpikeUserKey;
import com.imooc.spike.redis.UserKey;
import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.util.MD5Util;
import com.imooc.spike.util.UUIDUtil;
import com.imooc.spike.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:07 18-4-17
 * @Modified By:
 **/
@Service
public class SpikeUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private SpikeUserMapper spikeUserMapper;
    @Autowired
    private RedisService redisService;
    public SpikeUser getById(long id) {
        return spikeUserMapper.getById(id);
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String inputPass = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        //判断手机号是否存在
        SpikeUser user = getById(Long.valueOf(mobile));
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(inputPass, dbSalt);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        String token = UUIDUtil.uuid();
        addCookie(response, token, user);

        return true;
    }

    private void addCookie(HttpServletResponse response, String token, SpikeUser user) {
        //生成Cookie，为了实现分布式Session
        redisService.set(SpikeUserKey.spikeUserKey, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SpikeUserKey.spikeUserKey.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public SpikeUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        //
        SpikeUser spikeUser =  redisService.get(SpikeUserKey.spikeUserKey, token, SpikeUser.class);
        //延长有效期
        if(spikeUser != null) {
            addCookie(response, token, spikeUser);
        }
        return spikeUser;
    }
}
