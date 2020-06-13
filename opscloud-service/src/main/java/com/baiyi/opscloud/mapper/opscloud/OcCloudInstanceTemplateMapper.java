package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudInstanceTemplateMapper extends Mapper<OcCloudInstanceTemplate> {

    List<OcCloudInstanceTemplate> fuzzyQueryOcCloudInstanceTemplateByParam(CloudInstanceTemplateParam.PageQuery pageQuery);
}