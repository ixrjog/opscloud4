package com.baiyi.opscloud.facade.aliyun.impl;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.builder.AliyunLogMemberBuilder;
import com.baiyi.opscloud.cloud.log.AliyunLogCenter;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.aliyun.log.AliyunLogDecorator;
import com.baiyi.opscloud.decorator.aliyun.log.AliyunLogMemberDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLog;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLogMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogMemberParam;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogVO;
import com.baiyi.opscloud.facade.aliyun.AliyunLogFacade;
import com.baiyi.opscloud.service.aliyun.log.OcAliyunLogMemberService;
import com.baiyi.opscloud.service.aliyun.log.OcAliyunLogService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/13 1:39 下午
 * @Version 1.0
 */
@Service
public class AliyunLogFacadeImpl implements AliyunLogFacade {

    @Resource
    private AliyunLogCenter aliyunLogCenter;

    @Resource
    private OcAliyunLogService ocAliyunLogService;

    @Resource
    private OcAliyunLogMemberService ocAliyunLogMemberService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private AliyunLogDecorator aliyunLogDecorator;

    @Resource
    private AliyunLogMemberDecorator aliyunLogMemberDecorator;

    @Override
    public DataTable<AliyunLogVO.Log> queryLogPage(AliyunLogParam.AliyunLogPageQuery pageQuery) {
        DataTable<OcAliyunLog> table = ocAliyunLogService.queryOcAliyunLogByParam(pageQuery);
        List<AliyunLogVO.Log> page = BeanCopierUtils.copyListProperties(table.getData(), AliyunLogVO.Log.class)
                .stream().map(e -> aliyunLogDecorator.decorator(e)).collect(Collectors.toList());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DataTable<AliyunLogMemberVO.LogMember> queryLogMemberPage(AliyunLogMemberParam.LogMemberPageQuery pageQuery) {
        DataTable<OcAliyunLogMember> table = ocAliyunLogMemberService.queryOcAliyunLogMemberByParam(pageQuery);
        List<AliyunLogMemberVO.LogMember> page = BeanCopierUtils.copyListProperties(table.getData(), AliyunLogMemberVO.LogMember.class)
                .stream().map(e -> aliyunLogMemberDecorator.decorator(e)).collect(Collectors.toList());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> updateLog(AliyunLogVO.Log log) {
        ocAliyunLogService.updateOcAliyunLog(BeanCopierUtils.copyProperties(log, OcAliyunLog.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addLog(AliyunLogVO.Log log) {
        ocAliyunLogService.addOcAliyunLog(BeanCopierUtils.copyProperties(log, OcAliyunLog.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<Project> getProjects(AliyunLogParam.ProjectQuery query) {
        return aliyunLogCenter.getProjects(query.getQueryName());
    }

    @Override
    public List<String> getLogStores(AliyunLogParam.LogStoreQuery query) {
        return aliyunLogCenter.getLogStores(query.getProjectName());
    }

    @Override
    public List<String> getConfigs(AliyunLogParam.ConfigQuery query) {
        return aliyunLogCenter.getConfigs(query.getProjectName(), query.getLogstoreName());
    }

    @Override
    public BusinessWrapper<Boolean> pushLogMember(int id) {
        OcAliyunLogMember ocAliyunLogMember = ocAliyunLogMemberService.queryOcAliyunLogMemberById(id);
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocAliyunLogMember.getServerGroupId());
        List<OcServer> servers =acqMemberServers(ocAliyunLogMember);
        AliyunLogMemberVO.LogMember logMember = BeanCopierUtils.copyProperties(ocAliyunLogMember, AliyunLogMemberVO.LogMember.class);
        logMember.setServerGroup(ocServerGroup);
        OcAliyunLog ocAliyunLog = ocAliyunLogService.queryOcAliyunLogById(ocAliyunLogMember.getLogId());
        logMember.setLog(BeanCopierUtils.copyProperties(ocAliyunLog, AliyunLogVO.Log.class));
        logMember.setMachineList(Lists.newArrayList(servers.stream().map(OcServer::getPrivateIp).collect(Collectors.toList())));
        updateOcAliyunLogMemberLastPushTime(ocAliyunLogMember);
        return aliyunLogCenter.pushMachineGroup(logMember);
    }

    private List<OcServer> acqMemberServers(OcAliyunLogMember ocAliyunLogMember) {
        List<OcServer> servers;
        if (ocAliyunLogMember.getEnvType() == 0) {
            servers = ocServerService.queryOcServerByServerGroupId(ocAliyunLogMember.getServerGroupId());
        } else {
            servers = ocServerService.queryOcServerByServerGroupIdAndEnvType(ocAliyunLogMember.getServerGroupId(),ocAliyunLogMember.getEnvType());
        }
        return servers.stream().filter(this::isFilterServer).collect(Collectors.toList());
    }

    private boolean isFilterServer(OcServer ocServer) {
        if (!ocServer.getIsActive()) return false; // 过滤无效服务器
        return (ocServer.getServerType() == CloudServerType.ECS.getType() || ocServer.getServerType() == CloudServerType.EC2.getType());
    }

    private void updateOcAliyunLogMemberLastPushTime(OcAliyunLogMember ocAliyunLogMember) {
        ocAliyunLogMember.setLastPushTime(new Date());
        ocAliyunLogMemberService.updateOcAliyunLogMember(ocAliyunLogMember);
    }

    @Override
    public BusinessWrapper<Boolean> pushLog(int id) {
        ocAliyunLogMemberService.queryOcAliyunLogMemberByLogId(id).forEach(e -> pushLogMember(e.getId()));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addLogMember(AliyunLogMemberParam.AddLogMember addLogMember) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(addLogMember.getServerGroupId());
        if (ocServerGroup == null) return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NOT_EXIST);
        OcAliyunLogMember pre = AliyunLogMemberBuilder.build(addLogMember, ocServerGroup);
        OcAliyunLogMember check = ocAliyunLogMemberService.queryOcAliyunLogMemberByUniqueKey(pre);
        if (check != null)
            ocAliyunLogMemberService.deleteOcAliyunLogMemberById(check.getId());
        ocAliyunLogMemberService.addOcAliyunLogMember(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> removeLogMember(int id) {
        ocAliyunLogMemberService.deleteOcAliyunLogMemberById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<OcServerGroup>> queryLogMemberServerGroupPage(ServerGroupParam.LogMemberServerGroupQuery pageQuery) {
        pageQuery.setPage(1);
        pageQuery.setLength(20);
        return new BusinessWrapper(ocServerGroupService.queryLogMemberOcServerGroupByParam(pageQuery));
    }

    @Override
    public void pushTask(Set<Integer> keySet) {
        keySet.forEach(e -> {
                    List<OcAliyunLogMember> members = ocAliyunLogMemberService.queryOcAliyunLogMemberByServerGroupId(e);
                    if (members != null)
                        members.forEach(m -> pushLogMember(m.getId()));
                }
        );
    }

}
