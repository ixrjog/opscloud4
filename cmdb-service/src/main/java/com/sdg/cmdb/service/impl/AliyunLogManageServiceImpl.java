package com.sdg.cmdb.service.impl;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.MachineGroup;
import com.aliyun.openservices.log.common.Project;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.sdg.cmdb.dao.cmdb.LogServiceDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.logService.MachineGroupVO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceServerGroupCfgVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceCfgVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceGroupVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceStatusVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceUserVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.AliyunLogManageService;
import com.sdg.cmdb.service.AliyunLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class AliyunLogManageServiceImpl implements AliyunLogManageService {

    @Resource
    private AliyunLogService aliyunLogService;

    @Resource
    private LogServiceDao logServiceDao;

    @Resource
    private ServerDao serverDao;


    public static final String LOG_SERVICE_CONFIG_NAME = "apps";


    private List<Project> queryListProject(String project) {
        Client client = aliyunLogService.acqClient();
        // 列出当前 project 下的所有日志库名称
        int offset = 0;
        int size = 100;
        String logStoreSubName = "";
        ListProjectRequest req = new ListProjectRequest(project, offset, size);
        List<Project> projects = new ArrayList<>();
        try {
            projects = client.ListProject(req).getProjects();
        } catch (LogException lg) {

        }
        return projects;
    }

    @Override
    public List<String> queryListProject() {
        List<Project> projects = queryListProject("");
        List<String> projectsName = new ArrayList<>();
        for (Project p : projects) {
            projectsName.add(p.getProjectName());
        }
        return projectsName;
    }


    @Override
    public List<String> queryListLogStores(String project) {
        Client client = aliyunLogService.acqClient();
        // 列出当前 project 下的所有日志库名称
        int offset = 0;
        int size = 100;
        String logStoreSubName = "";
        ListLogStoresRequest req = new ListLogStoresRequest(project, offset, size, logStoreSubName);
        ArrayList<String> logStores = new ArrayList<>();
        try {
            logStores = client.ListLogStores(req).GetLogStores();
        } catch (LogException lg) {

        }
        return logStores;
    }


    @Override
    public List<String> queryListMachineGroup(String project, String groupName) {
        Client client = aliyunLogService.acqClient();
        // 列出当前 project 下的所有日志库名称
        int offset = 0;
        int size = 50;
        ListMachineGroupRequest req = new ListMachineGroupRequest(project, groupName, offset, size);
        List<String> machineGroups = new ArrayList<>();
        try {
            machineGroups = client.ListMachineGroup(req).GetMachineGroups();
        } catch (LogException lg) {

        }
        return machineGroups;
    }

    @Override
    public MachineGroup getMachineGroup(String project, String groupName) {

        Client client = aliyunLogService.acqClient();
        GetMachineGroupRequest req = new GetMachineGroupRequest(project, groupName);
        MachineGroup machineGroup = new MachineGroup();
        try {
            machineGroup = client.GetMachineGroup(req).GetMachineGroup();
        } catch (LogException lg) {

        }

        return machineGroup;
    }

    @Override
    public MachineGroupVO getMachineGroupVO(String project, String groupName) {
        MachineGroup machineGroup = getMachineGroup(project, groupName);
        if (machineGroup == null) return new MachineGroupVO();
        List<ServerDO> serverList = new ArrayList<>();
        for (String ip : machineGroup.GetMachineList()) {
            ServerDO serverDO = serverDao.queryServerByInsideIp(ip);
            if (serverDO != null)
                serverList.add(serverDO);
        }
        MachineGroupVO machineGroupVO = new MachineGroupVO(machineGroup, serverList);
        return machineGroupVO;
    }

    /**
     * 保存机器组
     *
     * @param cfgVO
     * @return
     */
    private boolean saveMachineGroup(LogServiceServerGroupCfgVO cfgVO) {
        MachineGroup machineGroup = getMachineGroup(cfgVO.acqProject(), cfgVO.getServerGroupDO().getName());
        if (machineGroup == null || StringUtils.isEmpty(machineGroup.GetGroupName())) {
            return addMachineGroup(cfgVO);
        } else {
            return updateMachineGroup(cfgVO);
        }
    }

    /**
     * 将配置应用到机器组
     *
     * @param project
     * @param groupName
     * @return
     */
    private boolean applyConfigToMachineGroup(String project, String groupName) {
        Client client = aliyunLogService.acqClient();
        ApplyConfigToMachineGroupRequest req = new ApplyConfigToMachineGroupRequest(project, groupName, LOG_SERVICE_CONFIG_NAME);
        try {
            client.ApplyConfigToMachineGroup(req);
            return true;
            //machineGroup = client.GetMachineGroup(req).GetMachineGroup();
        } catch (LogException lg) {
            return false;
        }


    }

    /**
     * 更新机器组
     *
     * @param cfgVO
     * @return
     */
    public boolean updateMachineGroup(LogServiceServerGroupCfgVO cfgVO) {
        MachineGroup machineGroup = new MachineGroup(cfgVO.getServerGroupName(), "ip", acqMachineList(cfgVO.getServerGroupId()));
        machineGroup.SetGroupTopic(cfgVO.getTopic());
        Client client = aliyunLogService.acqClient();
        try {
            UpdateMachineGroupRequest req = new UpdateMachineGroupRequest(cfgVO.getProject(), machineGroup);
            client.UpdateMachineGroup(req);
            return true;
        } catch (LogException lg) {
            return false;
        }
    }

    /**
     * 添加机器组
     *
     * @param cfgVO
     * @return
     */
    public boolean addMachineGroup(LogServiceServerGroupCfgVO cfgVO) {
        MachineGroup machineGroup = new MachineGroup(cfgVO.getServerGroupName(), "ip", acqMachineList(cfgVO.getServerGroupId()));
        machineGroup.SetGroupTopic(cfgVO.getTopic());
        Client client = aliyunLogService.acqClient();
        try {
            CreateMachineGroupRequest req = new CreateMachineGroupRequest(cfgVO.getProject(), machineGroup);
            client.CreateMachineGroup(req);
            applyConfigToMachineGroup(cfgVO.acqProject(), cfgVO.getServerGroupDO().getName());
            return true;
        } catch (LogException lg) {
            return false;
        }
    }

    // 获取服务器组的IP列表（用于生成机器组）
    private ArrayList<String> acqMachineList(long serverGroupId) {
        List<ServerDO> servers = serverDao.acqServersByGroupId(serverGroupId);
        ArrayList<String> machineList = new ArrayList<>();
        for (ServerDO serverDO : servers) {
            if (serverDO.getEnvType() == ServerDO.EnvTypeEnum.prod.getCode())
                machineList.add(serverDO.getInsideIp());
            if (serverDO.getEnvType() == ServerDO.EnvTypeEnum.back.getCode())
                machineList.add(serverDO.getInsideIp());
            if (serverDO.getEnvType() == ServerDO.EnvTypeEnum.gray.getCode())
                machineList.add(serverDO.getInsideIp());
        }
        return machineList;
    }


    @Override
    public BusinessWrapper<Boolean> saveServerGroupCfg(LogServiceServerGroupCfgVO cfgVO) {
        if (StringUtils.isEmpty(cfgVO.getTopic()))
            cfgVO.setTopic(cfgVO.getServerGroupDO().getName());
        if (StringUtils.isEmpty(cfgVO.getServerGroupName()))
            cfgVO.setServerGroupName(cfgVO.getServerGroupDO().getName());
        if (cfgVO.getServerGroupId() == 0)
            cfgVO.setServerGroupId(cfgVO.getServerGroupDO().getId());

        if (!saveMachineGroup(cfgVO))
            return new BusinessWrapper<>(false);
        try {
            if (cfgVO.getId() == 0) {
                logServiceDao.addLogServiceServerGroupCfg(cfgVO);
            } else {
                logServiceDao.updateLogServiceServerGroupCfg(cfgVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public LogServiceStatusVO logServiceStatus() {
        LogServiceStatusVO statusVO = new LogServiceStatusVO();
        //统计 1天，7天，30天的查询次数
        HashMap<String, Integer> cntMap = new HashMap<>();
        cntMap.put("day", logServiceDao.statusLogServiceCntByDay(1));
        cntMap.put("week", logServiceDao.statusLogServiceCntByDay(7));
        cntMap.put("month", logServiceDao.statusLogServiceCntByDay(30));
        statusVO.setCntMap(cntMap);

        //统计配置
        int groupCnt = logServiceDao.getLogServiceServerGroupCfgCnt();
        int serverCnt = logServiceDao.getLogServiceServerGroupServerCnt();
        LogServiceCfgVO logServiceCfgVO = new LogServiceCfgVO(groupCnt, serverCnt);
        statusVO.setLogServiceCfgVO(logServiceCfgVO);

        //统计热门项目
        List<LogServiceGroupVO> topGroupList = logServiceDao.statusLogServiceGroup();
        statusVO.setTopGroupList(topGroupList);

        //统计热门用户
        List<LogServiceUserVO> topUserList = logServiceDao.statusLogServiceUser();
        statusVO.setTopUserList(topUserList);

        return statusVO;

    }


}
