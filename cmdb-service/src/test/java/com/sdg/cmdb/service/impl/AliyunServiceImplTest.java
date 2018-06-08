package com.sdg.cmdb.service.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AliyunServiceImplTest {


    @Resource
    private AliyunServiceImpl aliyunServiceImpl;


    @Test
    public void testGetDescribeRegions() {
        aliyunServiceImpl.getDescribeRegions();
    }


    @Test
    public void testGetDescribeVpcs() {
        List<DescribeVpcsResponse.Vpc> vpcs = aliyunServiceImpl.getDescribeVpcs();

        for (DescribeVpcsResponse.Vpc vpc : vpcs) {
            System.err.println(vpc.getVpcId());
            // 描述
            System.err.println(vpc.getDescription());
            // 自定义名称
            System.err.println(vpc.getVpcName());

            System.err.println("vSwitchIds:----------------");
            printVSwitchs(vpc.getVSwitchIds());
            System.err.println("vSwitchIds:----------------");
        }
    }

    private void printVSwitchs(List<String> vSwitchIds) {
        for (String id : vSwitchIds)
            System.err.println(id);

    }

    @Test
    public void testRsyncAliyunNetwork() {
        System.err.println(  aliyunServiceImpl.rsyncAliyunNetwork());
    }

}
