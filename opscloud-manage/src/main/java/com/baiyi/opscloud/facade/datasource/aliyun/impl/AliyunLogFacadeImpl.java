package com.baiyi.opscloud.facade.datasource.aliyun.impl;

import com.aliyun.openservices.log.common.MachineGroup;
import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.aliyun.log.drive.AliyunLogDrive;
import com.baiyi.opscloud.datasource.aliyun.log.drive.AliyunLogMachineGroupDrive;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogMemberParam;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogParam;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;
import com.baiyi.opscloud.facade.datasource.aliyun.AliyunLogFacade;
import com.baiyi.opscloud.packer.datasource.aliyun.AliyunLogMemberPacker;
import com.baiyi.opscloud.packer.datasource.aliyun.AliyunLogPacker;
import com.baiyi.opscloud.service.datasource.AliyunLogMemberService;
import com.baiyi.opscloud.service.datasource.AliyunLogService;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;

/**
 * @Author baiyi
 * @Date 2021/9/16 5:32 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AliyunLogFacadeImpl implements AliyunLogFacade {

    private final AliyunLogService aliyunLogService;

    private final AliyunLogMemberService aliyunLogMemberService;

    private final AliyunLogDrive aliyunLogHandler;

    private final ServerGroupService serverGroupService;

    private final AliyunLogPacker aliyunLogPacker;

    private final AliyunLogMemberPacker aliyunLogMemberPacker;

    private final AliyunLogMachineGroupDrive aliyunLogMachineGroupHandler;

    private final DsConfigService dsConfigService;

    private final DsConfigHelper dsFactory;

    private final DsInstanceService dsInstanceService;

    @Override
    public DataTable<AliyunLogVO.Log> queryLogPage(AliyunLogParam.AliyunLogPageQuery pageQuery) {
        DataTable<AliyunLog> table = aliyunLogService.queryAliyunLogByParam(pageQuery);
        return new DataTable<>(table.getData().stream().map(e -> aliyunLogPacker.wrap(e, pageQuery)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<AliyunLogMemberVO.LogMember> queryLogMemberPage(AliyunLogMemberParam.LogMemberPageQuery pageQuery) {
        DataTable<AliyunLogMember> table = aliyunLogMemberService.queryAliyunLogMemberByParam(pageQuery);

        List<AliyunLogMemberVO.LogMember> data = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(table.getData())) {
            data = BeanCopierUtil.copyListProperties(table.getData(), AliyunLogMemberVO.LogMember.class).stream().peek(e -> aliyunLogMemberPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        }
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void updateLog(AliyunLogVO.Log log) {
        aliyunLogService.update(BeanCopierUtil.copyProperties(log, AliyunLog.class));
    }

    @Override
    public void addLog(AliyunLogVO.Log log) {
        aliyunLogService.add(BeanCopierUtil.copyProperties(log, AliyunLog.class));
    }

    @Override
    public void deleteLogById(Integer id) {
        if (aliyunLogMemberService.countByAliyunLogId(id) > 0)
            throw new CommonRuntimeException("日志服务成员未删除！");
        aliyunLogService.deleteById(id);
    }

    @Override
    public List<Project> queryProject(AliyunLogParam.ProjectQuery query) {
        AliyunConfig aliyunDsInstanceConfig = (AliyunConfig) getConfig(query.getInstanceId());
        return aliyunLogHandler.listProject(aliyunDsInstanceConfig.getAliyun(), query.getQueryName());
    }

    @Override
    public List<String> queryLogstore(AliyunLogParam.LogStoreQuery query) {
        AliyunConfig aliyunDsInstanceConfig = (AliyunConfig) getConfig(query.getInstanceId());
        return aliyunLogHandler.listLogstore(aliyunDsInstanceConfig.getAliyun(), query.getProjectName());
    }

    @Override
    public List<String> queryConfig(AliyunLogParam.ConfigQuery query) {
        AliyunConfig aliyunDsInstanceConfig = (AliyunConfig) getConfig(query.getInstanceId());
        return aliyunLogHandler.listConfig(aliyunDsInstanceConfig.getAliyun(), query.getProjectName(), query.getLogstoreName());
    }

    @Async(value = CORE)
    @Override
    public void pushLogMemberByServerGroupId(Integer id) {
        List<AliyunLogMember> aliyunLogMembers = aliyunLogMemberService.queryByServerGroupId(id);
        if (CollectionUtils.isEmpty(aliyunLogMembers)) return;
        aliyunLogMembers.forEach(e -> pushLogMemberById(e.getId()));
    }

    @Override
    public void pushLogMemberById(Integer id) {
        AliyunLogMember aliyunLogMember = aliyunLogMemberService.getById(id);
        if (aliyunLogMember == null) return;
        AliyunLogMemberVO.LogMember logMember = BeanCopierUtil.copyProperties(aliyunLogMember, AliyunLogMemberVO.LogMember.class);
        aliyunLogMemberPacker.wrap(logMember, SimpleExtend.EXTEND);
        AliyunConfig aliyunDsInstanceConfig = (AliyunConfig) getConfig(logMember.getLog().getDatasourceInstanceId());
        MachineGroup machineGroup = aliyunLogMachineGroupHandler.getMachineGroup(aliyunDsInstanceConfig.getAliyun(), logMember.getLog().getProject(), logMember.getServerGroupName());
        if (machineGroup == null) {
            aliyunLogMachineGroupHandler.createMachineGroup(aliyunDsInstanceConfig.getAliyun(), logMember); // 创建
        } else {
            aliyunLogMachineGroupHandler.updateMachineGroup(aliyunDsInstanceConfig.getAliyun(), logMember); // 更新
        }
        updateAliyunLogMemberLastPushTime(aliyunLogMember);
    }

    private void updateAliyunLogMemberLastPushTime(AliyunLogMember aliyunLogMember) {
        aliyunLogMember.setLastPushTime(new Date());
        aliyunLogMemberService.update(aliyunLogMember);
    }

    @Override
    public void pushLogById(Integer id) {
        List<AliyunLogMember> aliyunLogMembers = aliyunLogMemberService.queryByAliyunLogId(id);
        if (CollectionUtils.isEmpty(aliyunLogMembers)) return;
        aliyunLogMembers.forEach(e -> pushLogMemberById(e.getId()));
    }

    @Override
    public void addLogMember(AliyunLogMemberVO.LogMember logMember) {
        ServerGroup serverGroup = serverGroupService.getById(logMember.getServerGroupId());
        if (serverGroup == null) return;
        AliyunLogMember pre = BeanCopierUtil.copyProperties(logMember, AliyunLogMember.class);
        pre.setServerGroupName(serverGroup.getName());
        aliyunLogMemberService.add(pre);
    }

    @Override
    public void updateLogMember(AliyunLogMemberVO.LogMember logMember) {
        AliyunLogMember pre = BeanCopierUtil.copyProperties(logMember, AliyunLogMember.class);
        aliyunLogMemberService.update(pre);
    }

    @Override
    public void deleteLogMemberById(Integer id) {
        aliyunLogMemberService.deleteById(id);
    }

    private BaseConfig getConfig(Integer datasourceInstanceId) {
        DatasourceInstance dsInstance = dsInstanceService.getById(datasourceInstanceId);
        DatasourceConfig datasourceConfig = dsConfigService.getById(dsInstance.getConfigId());
        return dsFactory.build(datasourceConfig, AliyunConfig.class);
    }


//    @Override
//    public BusinessWrapper<List<OcServerGroup>> queryLogMemberServerGroupPage(ServerGroupParam.LogMemberServerGroupQuery pageQuery) {
//        pageQuery.setPage(1);
//        pageQuery.setLength(20);
//        return new BusinessWrapper<>(ocServerGroupService.queryLogMemberOcServerGroupByParam(pageQuery));
//    }


}

