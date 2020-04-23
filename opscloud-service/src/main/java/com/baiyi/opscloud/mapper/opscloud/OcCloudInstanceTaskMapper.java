package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTask;
import tk.mybatis.mapper.common.Mapper;

public interface OcCloudInstanceTaskMapper extends Mapper<OcCloudInstanceTask> {

    OcCloudInstanceTask queryLastOcCloudInstanceTaskByTemplateId(int templateId);
}