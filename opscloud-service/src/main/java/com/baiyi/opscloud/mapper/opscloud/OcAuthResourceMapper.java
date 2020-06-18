package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthResource;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthResourceMapper extends Mapper<OcAuthResource> {

    List<OcAuthResource> queryOcAuthResourceByParam(ResourceParam.PageQuery pageQuery);

    List<OcAuthResource> queryOcAuthRoleBindResourceByParam(ResourceParam.BindResourcePageQuery pageQuery);

    List<OcAuthResource> queryOcAuthRoleUnbindResourceByParam(ResourceParam.BindResourcePageQuery pageQuery);

}