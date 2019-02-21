package com.sdg.cmdb.service;


import com.aliyun.oss.model.OSSObjectSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AliyunOssServiceTest {

    @Autowired
    private AliyunOssService aliyunOssService;

    @Test
    public void testListObject() {
        List<OSSObjectSummary> objects = aliyunOssService.listObject("ci/java_opscloud_prod/184/opscloud-1.0.0-SNAPSHOT.war");
        for(OSSObjectSummary object :objects){
            System.err.println(object.getBucketName());
            System.err.println(object.getKey());
        }
    }

}
