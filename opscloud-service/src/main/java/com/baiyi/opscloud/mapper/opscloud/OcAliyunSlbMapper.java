package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunSlbMapper extends Mapper<OcAliyunSlb> {

    List<OcAliyunSlb> queryOcAliyunSlbPage(AliyunSLBParam.PageQuery pageQuery);

    List<OcAliyunSlb> queryOcAliyunSlbByLoadBalancerIdList(@Param("loadBalancerIdList") List<String> loadBalancerIdList);
}