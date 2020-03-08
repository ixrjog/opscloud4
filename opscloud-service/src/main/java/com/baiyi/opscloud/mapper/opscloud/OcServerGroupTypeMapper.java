package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroupType;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerGroupTypeMapper extends Mapper<OcServerGroupType> {

    List<OcServerGroupType> queryOcServerGroupTypeByParam(ServerGroupTypeParam.PageQuery pageQuery);
}