package com.baiyi.opscloud.cloud.log.impl;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.aliyun.log.handler.AliyunLogHandler;
import com.baiyi.opscloud.cloud.log.AliyunLogCenter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 1:43 下午
 * @Version 1.0
 */
@Component
public class AliyunLogCenterImpl implements AliyunLogCenter {

    @Resource
    private AliyunLogHandler aliyunLogHandler;

    @Override
    public List<Project> getProjects(String project) {
       // return aliyunLogHandler.getProjects(project).stream().map(e -> e.getProjectName()).collect(Collectors.toList());

       return aliyunLogHandler.getProjects(project);
    }
}
