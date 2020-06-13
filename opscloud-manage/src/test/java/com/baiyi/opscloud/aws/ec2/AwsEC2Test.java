package com.baiyi.opscloud.aws.ec2;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aws.ec2.base.EC2InstanceType;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/13 3:38 下午
 * @Version 1.0
 */
@EnableCaching
public class AwsEC2Test extends BaseUnit {

    @Resource
    private AwsEC2 awsEC2;

    @Test
    void test() {
        String[] typeList = {"c5d.4xlarge", "c3.8xlarge","c5.xlarge","m5.large"};
        for(String type : typeList)
            System.err.println(JSON.toJSONString(getEC2InstanceType(type)));
    }

    private EC2InstanceType getEC2InstanceType(String type) {
        return awsEC2.getInstanceType(type);
    }

}
