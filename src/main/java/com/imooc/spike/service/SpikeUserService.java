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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

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
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpikeUserMapper spikeUserMapper;
    //service和service之间互相调用，不同service不要去调用别的dao
    @Autowired
    private RedisService redisService;

    public SpikeUser getById(long id) {
        SpikeUser user = redisService.get(SpikeUserKey.getById, "" + id, SpikeUser.class);
        if(user != null) {
            log.info("成功从缓存获取用户!");
            return user;
        }
        user = spikeUserMapper.getById(id);
        //存缓存
        redisService.set(SpikeUserKey.getById, "" + id, user);
        return user;
    }

    //更新密码
    public boolean updatePass(String token, long id, String passwordNew) {
        //取user
        SpikeUser user = getById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_ERROR);
        }
        //更新数据库
        SpikeUser toBeUpdate = new SpikeUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew, user.getSalt()));
        spikeUserMapper.update(toBeUpdate);
        //处理缓存
        redisService.del(SpikeUserKey.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SpikeUserKey.spikeUserKey, "" + id, token);
        return true;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String inputPass = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        //判断手机号是否存在
        SpikeUser user = getById(Long.valueOf(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(inputPass, dbSalt);
        if (!calcPass.equals(dbPass)) {
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
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        //对象缓存
        SpikeUser spikeUser = redisService.get(SpikeUserKey.spikeUserKey, token, SpikeUser.class);
        //延长有效期
        if (spikeUser != null) {
            addCookie(response, token, spikeUser);
        }
        return spikeUser;
    }
}
