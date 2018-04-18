package com.imooc.spike.mapper;

import com.imooc.spike.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午6:14 18-4-17
 * @Modified By:
 **/
@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    public User getById(int id);

}
