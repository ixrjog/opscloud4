package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunOnsTopicMapper extends Mapper<OcAliyunOnsTopic> {

    List<OcAliyunOnsTopic> queryOcAliyunOnsTopicByInstanceId(AliyunONSParam.TopicPageQuery pageQuery);
}