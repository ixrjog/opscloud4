package com.sdg.cmdb.service.impl;


import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.MachineGroup;
import com.aliyun.openservices.log.common.Project;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.ApplyConfigToMachineGroupResponse;
import com.sdg.cmdb.dao.cmdb.LogServiceDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.logService.LogServiceGroupCfgDO;
import com.sdg.cmdb.domain.logService.LogServiceGroupCfgVO;
import com.sdg.cmdb.domain.logService.LogServiceMemberDO;
import com.sdg.cmdb.domain.logService.MachineGroupVO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceServerGroupCfgVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceCfgVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceGroupVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceStatusVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceUserVO;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.AliyunLogManageService;
import com.sdg.cmdb.service.AliyunLogService;
import com.sdg.cmdb.service.ConfigServerGroupService;
import com.sdg.cmdb.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class AliyunLogManageServiceImpl implements AliyunLogManageService {

    @Autowired
    private AliyunLogService aliyunLogService;

    @Autowired
    private LogServiceDao logServiceDao;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Autowired
    private ConfigServerGroupService configServerGroupService;


    public static final String LOG_SERVICE_CONFIG_NAME = "apps";

    @Override
    public  List<String> queryListConfig(String project,String logstore) {
        Client client = aliyunLogService.acqClient();
        // 列出当前 project 下的所有日志库名称
        int offset = 0;
        int size = 100;
        String logStoreSubName = "";
        ListConfigRequest req = new ListConfigRequest(project, offset, size);
        req.SetLogstoreName(logstore);

        try {
            List<String> configs = client.ListConfig(req).GetConfigs();
            return configs;
        } catch (LogException lg) {

        }
        return new ArrayList<>();
    }


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
        serverList.sort(Comparator.naturalOrder());
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
    private boolean applyConfigToMachineGroup(String project, String groupName, String configName) {
        Client client = aliyunLogService.acqClient();
        ApplyConfigToMachineGroupRequest req = new ApplyConfigToMachineGroupRequest(project, groupName, configName);
        try {
            ApplyConfigToMachineGroupResponse response = client.ApplyConfigToMachineGroup(req);
            //System.err.println(JSON.toJSONString(response));
            return true;
            //machineGroup = client.GetMachineGroup(req).GetMachineGroup();
        } catch (LogException lg) {
            lg.printStackTrace();
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
        if(!StringUtils.isEmpty(cfgVO.getTopic())){
            machineGroup.SetGroupTopic(cfgVO.getTopic());
        }else{
            machineGroup.SetGroupTopic(configServerGroupService.queryLogServiceTopic(new ServerGroupDO(cfgVO.getServerGroupId())));
        }

        Client client = aliyunLogService.acqClient();
        try {
            UpdateMachineGroupRequest req = new UpdateMachineGroupRequest(cfgVO.getProject(), machineGroup);
            client.UpdateMachineGroup(req);
            return true;
        } catch (LogException lg) {
            return false;
        }
    }


    public boolean updateMachineGroup(LogServiceGroupCfgDO groupCfg, ServerGroupDO serverGroupDO) {
        MachineGroup machineGroup = new MachineGroup(serverGroupDO.getName(), "ip", acqMachineList(serverGroupDO.getId()));
        machineGroup.SetGroupTopic(configServerGroupService.queryLogServiceTopic(serverGroupDO));
        Client client = aliyunLogService.acqClient();
        try {
            UpdateMachineGroupRequest req = new UpdateMachineGroupRequest(groupCfg.getProject(), machineGroup);
            client.UpdateMachineGroup(req);
            applyConfigToMachineGroup(groupCfg.getProject(), serverGroupDO.getName(), groupCfg.getConfig());
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
        if(!StringUtils.isEmpty(cfgVO.getTopic())){
            machineGroup.SetGroupTopic(cfgVO.getTopic());
        }else{
            machineGroup.SetGroupTopic(configServerGroupService.queryLogServiceTopic(new ServerGroupDO(cfgVO.getServerGroupId())));
        }
        Client client = aliyunLogService.acqClient();
        try {
            CreateMachineGroupRequest req = new CreateMachineGroupRequest(cfgVO.getProject(), machineGroup);
            client.CreateMachineGroup(req);
            // 应用配置
            applyConfigToMachineGroup(cfgVO.acqProject(), cfgVO.getServerGroupDO().getName(), cfgVO.getLogstore());
            return true;
        } catch (LogException lg) {
            return false;
        }
    }

    private boolean addMachineGroup(LogServiceGroupCfgDO groupCfg, ServerGroupDO serverGroupDO) {
        MachineGroup machineGroup = new MachineGroup(serverGroupDO.getName(), "ip", acqMachineList(serverGroupDO.getId()));
        machineGroup.SetGroupTopic(configServerGroupService.queryLogServiceTopic(serverGroupDO));
        Client client = aliyunLogService.acqClient();
        try {
            CreateMachineGroupRequest req = new CreateMachineGroupRequest(groupCfg.getProject(), machineGroup);
            client.CreateMachineGroup(req);
            // 应用配置
            applyConfigToMachineGroup(groupCfg.getProject(), serverGroupDO.getName(), groupCfg.getConfig());
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
            if (serverDO.getEnvType() == EnvType.EnvTypeEnum.prod.getCode())
                machineList.add(serverDO.getInsideIp());
            if (serverDO.getEnvType() == EnvType.EnvTypeEnum.back.getCode())
                machineList.add(serverDO.getInsideIp());
            if (serverDO.getEnvType() == EnvType.EnvTypeEnum.gray.getCode())
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

    @Override
    public TableVO<List<LogServiceGroupCfgVO>> getLogServiceGroupPage(String project, String logstore, int page, int length) {
        long size = logServiceDao.getLogServiceGroupSize(project, logstore);
        List<LogServiceGroupCfgDO> list = logServiceDao.getLogServiceGroupPage(project, logstore, page * length, length);
        List<LogServiceGroupCfgVO> voList = new ArrayList<>();
        for (LogServiceGroupCfgDO logServiceGroupCfgDO : list) {
            LogServiceGroupCfgVO logServiceGroupCfgVO = BeanCopierUtils.copyProperties(logServiceGroupCfgDO, LogServiceGroupCfgVO.class);
            List<LogServiceMemberDO> memberList = logServiceDao.getLogServiceMemberByGroupCfgId(logServiceGroupCfgDO.getId());
            logServiceGroupCfgVO.setMemberList(memberList);
            voList.add(logServiceGroupCfgVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public LogServiceGroupCfgVO getLogServiceGroup(long id) {
        LogServiceGroupCfgDO logServiceGroupCfgDO = logServiceDao.getLogServiceGroup(id);
        LogServiceGroupCfgVO logServiceGroupCfgVO = BeanCopierUtils.copyProperties(logServiceGroupCfgDO, LogServiceGroupCfgVO.class);
        logServiceGroupCfgVO.setMemberList(logServiceDao.getLogServiceMemberByGroupCfgId(logServiceGroupCfgDO.getId()));
        return logServiceGroupCfgVO;
    }

    @Override
    public BusinessWrapper<Boolean> addMember(long groupCfgId, long serverGroupId) {
        try {
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
            LogServiceMemberDO logServiceMemberDO = new LogServiceMemberDO(groupCfgId, serverGroupDO);
            logServiceDao.addLogServiceMember(logServiceMemberDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delMember(long id) {
        try {
            logServiceDao.delLogServiceMember(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    /**
     * 推送配置到阿里云日志服务
     *
     * @param groupCfgId
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> pushGroupCfg(long groupCfgId) {
        LogServiceGroupCfgDO groupCfg = logServiceDao.getLogServiceGroup(groupCfgId);
        List<LogServiceMemberDO> memberList = logServiceDao.getLogServiceMemberByGroupCfgId(groupCfgId);
        log.info("日志服务配置同步：{}",groupCfg.toString());
        for (LogServiceMemberDO logServiceMemberDO : memberList) {
            try {
                ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(logServiceMemberDO.getServerGroupId());
                log.info("日志服务配置同步：服务器组{}",serverGroupDO.getName());
                if (serverGroupDO == null) {
                    logServiceDao.delLogServiceMember(logServiceMemberDO.getId());
                    continue;
                }
                MachineGroup machineGroup = getMachineGroup(groupCfg.getProject(), serverGroupDO.getName());
                if (machineGroup == null || StringUtils.isEmpty(machineGroup.GetGroupName())) {
                   addMachineGroup(groupCfg, serverGroupDO);
                } else {
                   updateMachineGroup(groupCfg, serverGroupDO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new BusinessWrapper<Boolean>(false);
    }

    @Override
    public BusinessWrapper<Boolean> saveGroupCfg(LogServiceGroupCfgDO logServiceGroupCfgDO) {
        try {
            if (logServiceGroupCfgDO.getId() == 0) {
                logServiceDao.addLogServiceGroupCfg(logServiceGroupCfgDO);
            } else {
                logServiceDao.updateLogServiceGroupCfg(logServiceGroupCfgDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }


}
