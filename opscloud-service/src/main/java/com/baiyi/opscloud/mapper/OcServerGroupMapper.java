package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerGroupMapper extends Mapper<OcServerGroup> {

    List<OcServerGroup> queryOcServerGroupByParam(ServerGroupParam.PageQuery pageQuery);

    List<OcServerGroup> queryUerPermissionOcServerGroupByUserId(@Param("userId") int userId);
}
