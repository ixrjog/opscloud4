package com.sdg.cmdb.service;


import com.alibaba.fastjson.JSON;
import com.aliyun.oss.model.OSSObjectSummary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Job;
import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.domain.ci.BuildArtifactDO;
import com.sdg.cmdb.domain.ci.CiBuildDO;
import com.sdg.cmdb.domain.ci.DeployInfo;
import com.sdg.cmdb.domain.ci.jobParametersYaml.JobParametersYaml;
import com.sdg.cmdb.service.impl.CiServiceImpl;
import com.sdg.cmdb.util.BeanCopierUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class CiServiceTest {

    @Autowired
    private CiService ciService;

    @Autowired
    private CiServiceImpl ciServiceImpl;


    @Autowired
    private JenkinsService jenkinsService;

    @Autowired
    private CiDao ciDao;

    @Test
    public void testVersion() {
        JenkinsVersion version = ciService.getJenkinsVersion();
        System.err.println(version);
    }

    @Test
    public void testGetJobs() {
        Map<String, Job> map = jenkinsService.getJobs();
        for(String key:map.keySet()){
            System.err.println(key);
        }
    }

    @Test
    public void testBuildJob() {
        ciService.buildJob(9);
    }

    @Test
    public void testInfo() {
        DeployInfo info = ciService.queryDeployInfo(1);
        System.err.println(info.toString());
    }

    @Test
    public void testOss() {

        //{"bucketName":"opscloud","eTag":"2F72B7215DCCC6CBA0613C48C25D612B",
        // "key":"android/android-globalagency-test/55/bm_release_[yyb].apk",
        // "lastModified":1555387228000,"owner":{"displayName":"1255805305757185","id":"1255805305757185"},"size":61174171,"storageClass":"IA"}

        CiBuildDO ciBuildDO =  ciDao.getBuild(454);
        BuildArtifactDO buildArtifactDO = ciDao.getBuildArtifact(8879);
        OSSObjectSummary ossObj = ciServiceImpl.getOssObject(ciBuildDO,buildArtifactDO);
        System.err.println(JSON.toJSONString(ossObj));
    }


    @Test
    public void test99() {
        //初始化Yaml解析器
        Yaml yaml = new Yaml();
        String yamlBody = "appName: opscloud\n" +
                "env: prod\n" +
                "version: 1.0.0\n" +
                "parameters: \n" +
                "  - \n" +
                "    name: ansibleHostPath\n" +
                "    value: /data/www/data/ansible/ansible_hosts\n" +
                "    description: \n" +
                "  - \n" +
                "    name: hostPattern\n" +
                "    value: opscloud-prod\n" +
                "    description: \n" +
                "  - \n" +
                "    name: copySrc\n" +
                "    value: cmdb-web/build/libs/\n" +
                "    description: \n" +
                "  - \n" +
                "    name: copyDest\n" +
                "    value: /data/www/update/default/\n" +
                "    description: \n" +
                "  - \n" +
                "    name: build\n" +
                "    value: clean war -DpkgName=opscloud -Denv=online -refresh-dependencies -Dorg.gradle.daemon=false\n" +
                "    description: \n" +
                "  - \n" +
                "    name: project\n" +
                "    value: opscloud\n" +
                "    description: \n" +
                "  - \n" +
                "    name: deployCmd\n" +
                "    value: /usr/local/prometheus/deploy/deploy_war -tomcat.app.name=opscloud -tomcat.service.restart=true\n" +
                "    description: \n" +
                "  - \n" +
                "    name: artifactPath\n" +
                "    value: cmdb-web/build/libs/\n" +
                "    description: \n" +
                "  - \n" +
                "    name: artifactFilter\n" +
                "    value: '*.war'\n" +
                "    description: ";
        //读入文件

        Object result= yaml.load(yamlBody);

        try{
            Gson gson = new GsonBuilder().create();
            JobParametersYaml jobParams = gson.fromJson(JSON.toJSONString(result), JobParametersYaml.class);
            System.out.println(JSON.toJSONString(jobParams));
        }catch (Exception e){


        }

        //JobParametersYaml jobParams = BeanCopierUtils.copyProperties(result,JobParametersYaml.class);
       // System.out.println(result.getClass());

    }



}
