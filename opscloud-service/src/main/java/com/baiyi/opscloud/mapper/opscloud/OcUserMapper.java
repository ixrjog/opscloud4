package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.OcUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OcUserMapper extends Mapper<OcUser> {

    OcUser queryByUsername(@Param("username") String username);

}