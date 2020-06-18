package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudImage;
import com.baiyi.opscloud.domain.param.cloud.CloudImageParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudImageMapper extends Mapper<OcCloudImage> {

    List<OcCloudImage> fuzzyQueryOcCloudImageByParam(CloudImageParam.PageQuery pageQuery);
}