package com.baiyi.opscloud.datasource.aws.ec2.feign;

import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/1/21 1:02 PM
 * @Version 1.0
 */
public interface AmazonEc2InstanceFeign {

    @RequestLine("GET {path}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Map<String, InstanceModel.EC2InstanceType> getInstances(@Param("path") String path);

}
