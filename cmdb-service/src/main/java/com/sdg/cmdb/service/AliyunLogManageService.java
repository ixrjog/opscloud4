package com.sdg.cmdb.service;

import com.aliyun.openservices.log.common.MachineGroup;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.logService.MachineGroupVO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceServerGroupCfgVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceStatusVO;


import java.util.List;

public interface AliyunLogManageService {


    /**
     * 列出日志服务中所有的project
     *
     * @return
     */
    List<String> queryListProject();

    /**
     * 列出项目下所有的logstore
     *
     * @param project
     * @return
     */
    List<String> queryListLogStores(String project);

    /**
     * 查询所有的机器组
     *
     * @param project
     * @param groupName
     * @return
     */
    List<String> queryListMachineGroup(String project, String groupName);


    /**
     * 获取机器组详细信息
     *
     * @param project
     * @param groupName
     * @return
     */
    MachineGroup getMachineGroup(String project, String groupName);


    MachineGroupVO getMachineGroupVO(String project, String groupName);


    BusinessWrapper<Boolean> saveServerGroupCfg(LogServiceServerGroupCfgVO logServiceServerGroupCfgVO);

    /**
     * 首页统计
     * @return
     */
    LogServiceStatusVO logServiceStatus();

}
