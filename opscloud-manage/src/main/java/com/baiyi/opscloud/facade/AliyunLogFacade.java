package com.baiyi.opscloud.facade;

import com.aliyun.openservices.log.common.Project;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 11:54 上午
 * @Version 1.0
 */
public interface AliyunLogFacade {

    List<Project> getProjects(String project);
}
