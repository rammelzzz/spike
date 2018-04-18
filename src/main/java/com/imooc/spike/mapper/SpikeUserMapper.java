package com.imooc.spike.mapper;

import com.imooc.spike.domain.SpikeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:06 18-4-17
 * @Modified By:
 **/
@Mapper
public interface SpikeUserMapper {

        @Select("select * from spike_user where id = #{id}")
        public SpikeUser getById(long id);

}
