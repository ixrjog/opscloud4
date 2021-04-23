package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/1 11:25 上午
 * @Version 1.0
 */
public interface OcCloudServerMapper extends Mapper<OcCloudServer> {

    List<OcCloudServer> queryOcCloudServerByParam(CloudServerParam.CloudServerPageQuery pageQuery);

    List<OcCloudServer> queryCloudServerChargeByParam(CloudServerParam.CloudServerChargePageQuery pageQuery);
}
