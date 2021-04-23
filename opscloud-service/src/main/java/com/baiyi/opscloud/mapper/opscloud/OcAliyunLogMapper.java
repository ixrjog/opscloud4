package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLog;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunLogMapper extends Mapper<OcAliyunLog> {

    List<OcAliyunLog> queryOcAliyunLogByParam(AliyunLogParam.AliyunLogPageQuery pageQuery);
}