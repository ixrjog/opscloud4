package com.baiyi.opscloud.cloud.log;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogMemberVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 1:43 下午
 * @Version 1.0
 */
public interface AliyunLogCenter {

    List<Project> getProjects(String project);

    List<String> getLogStores(String projectName);

    List<String> getConfigs(String projectName, String logstoreName);

    BusinessWrapper<Boolean> pushMachineGroup(AliyunLogMemberVO.LogMember logMember);
}
