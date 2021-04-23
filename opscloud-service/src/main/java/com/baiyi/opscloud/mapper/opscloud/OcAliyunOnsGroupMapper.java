package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunOnsGroupMapper extends Mapper<OcAliyunOnsGroup> {

    List<OcAliyunOnsGroup> queryONSGroupPage(AliyunONSParam.GroupPageQuery pageQuery);
}