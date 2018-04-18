package com.imooc.spike.service;

import com.imooc.spike.domain.User;
import com.imooc.spike.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午6:15 18-4-17
 * @Modified By:
 **/
@Service
public class UserService {

        @Autowired
        private UserMapper userMapper;

        public User getById(int id) {
                return userMapper.getById(id);
        }

}
