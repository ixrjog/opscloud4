package com.baiyi.opscloud.cloud.log;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.BaseUnit;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 1:51 下午
 * @Version 1.0
 */
public class AliyunLogTest extends BaseUnit {

    @Resource
    private AliyunLogCenter aliyunLogCenter;


    @Test
    void testGetProjects() {
        List<Project> projects = aliyunLogCenter.getProjects("");
        System.err.println(JSON.toJSONString(projects));
    }

}