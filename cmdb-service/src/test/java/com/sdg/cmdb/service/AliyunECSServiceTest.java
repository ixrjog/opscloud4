package com.sdg.cmdb.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AliyunECSServiceTest {

    @Autowired
    private AliyunInstanceService instanceService;

    @Test
    public void test() {
        List<DescribeInstancesResponse.Instance> instanceList = instanceService.getInstanceList(null);
        System.err.println("ECS总数：" +instanceList.size());
        for(DescribeInstancesResponse.Instance instance: instanceList)
            System.err.println(JSON.toJSONString(instance));
    }

}
