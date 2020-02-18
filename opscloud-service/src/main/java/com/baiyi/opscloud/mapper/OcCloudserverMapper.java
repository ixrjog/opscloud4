package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.domain.param.cloudserver.CloudserverParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudserverMapper extends Mapper<OcCloudserver> {

    List<OcCloudserver> queryOcCloudserverByParam(CloudserverParam.PageQuery pageQuery);
}