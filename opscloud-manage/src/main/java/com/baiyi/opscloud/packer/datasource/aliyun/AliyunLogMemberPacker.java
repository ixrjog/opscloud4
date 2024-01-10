package com.baiyi.opscloud.packer.datasource.aliyun;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLog;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.datasource.AliyunLogService;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/17 11:13 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunLogMemberPacker implements IWrapper<AliyunLogMemberVO.LogMember> {

    private final ServerGroupService serverGroupService;

    private final AliyunLogService aliyunLogService;

    private final ServerService serverService;

    @Override
    @EnvWrapper
    @AgoWrapper
    public void wrap(AliyunLogMemberVO.LogMember logMember, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        ServerGroup serverGroup = serverGroupService.getById(logMember.getServerGroupId());
        logMember.setServerGroup(serverGroup);
        List<Server> servers = acqMemberServers(logMember);
        AliyunLog aliyunLog = aliyunLogService.getById(logMember.getAliyunLogId());
        logMember.setLog(BeanCopierUtil.copyProperties(aliyunLog, AliyunLogVO.Log.class));
        logMember.setMachineList(Lists.newArrayList(servers.stream().map(Server::getPrivateIp).collect(Collectors.toList())));
    }

    private List<Server> acqMemberServers(AliyunLogMemberVO.LogMember logMember) {
        List<Server> servers = logMember.getEnvType() == 0 ?
                serverService.queryByServerGroupId(logMember.getServerGroupId()) :
                serverService.queryByGroupIdAndEnvType(logMember.getServerGroupId(), logMember.getEnvType());
        return servers.stream().filter(Server::getIsActive).collect(Collectors.toList());
    }

}