package com.baiyi.opscloud.facade.impl;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.cloud.log.AliyunLogCenter;
import com.baiyi.opscloud.facade.AliyunLogFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 1:39 下午
 * @Version 1.0
 */
@Service
public class AliyunLogFacadeImpl implements AliyunLogFacade {

    @Resource
    private AliyunLogCenter aliyunLogCenter;

    public List<Project> getProjects(String project) {
        return aliyunLogCenter.getProjects(project);
    }
}
