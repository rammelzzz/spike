package com.imooc.spike.mapper;

import com.imooc.spike.domain.SpikeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:06 18-4-17
 * @Modified By:
 **/
@Mapper
public interface SpikeUserMapper {

    @Select("select * from spike_user where id = #{id}")
    SpikeUser getById(long id);

    @Update("update spike_user set password = #{password} where id = #{id}")
    int update(SpikeUser user);

}
