package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunSlbScMapper extends Mapper<OcAliyunSlbSc> {
    List<OcAliyunSlbSc> queryOcAliyunSlbScPage(AliyunSLBParam.SCPageQuery pageQuery);
}