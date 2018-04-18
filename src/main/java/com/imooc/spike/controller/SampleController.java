package com.imooc.spike.controller;

import com.imooc.spike.domain.User;
import com.imooc.spike.redis.RedisService;
import com.imooc.spike.redis.UserKey;
import com.imooc.spike.result.Result;
import com.imooc.spike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午1:09 18-4-16
 * @Modified By:
 **/
@Controller
@RequestMapping("/demo")
public class SampleController {


    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    /**
     * test
     * @param model
     * @return
     */
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "rammel");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> get() {
        return Result.success(userService.getById(1));
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<Long> redisGet() {
        Long v = redisService.get(UserKey.getById, "key1", Long.class);
        return Result.success(v);
    }

    @RequestMapping("/redis/test")
    @ResponseBody
    public Result<User> redisSetAndGet() {
        User user = new User();
        user.setId(1);
        user.setName("rammel");
        redisService.set(UserKey.getById, "1", user);
        User user2 = redisService.get(UserKey.getById, "1", User.class);
        return Result.success(user2);
    }

}
