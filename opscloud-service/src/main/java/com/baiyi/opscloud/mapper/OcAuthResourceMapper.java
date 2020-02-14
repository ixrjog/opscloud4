package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcAuthResource;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthResourceMapper extends Mapper<OcAuthResource> {

    List<OcAuthResource> queryOcAuthResourceByParam(ResourceParam.PageQuery pageQuery);

}