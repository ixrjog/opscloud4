package com.baiyi.opscloud.packer.datasource.aliyun;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.time.AgoUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLog;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLogMember;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;
import com.baiyi.opscloud.packer.base.IPacker;
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
public class AliyunLogMemberPacker implements IPacker<AliyunLogMemberVO.LogMember, AliyunLogMember> {

    private final ServerGroupService serverGroupService;

    private final AliyunLogService aliyunLogService;

    private final ServerService serverService;

    public AliyunLogMemberVO.LogMember toVO(AliyunLogMember aliyunLogMember) {
        return BeanCopierUtil.copyProperties(aliyunLogMember, AliyunLogMemberVO.LogMember.class);
    }

    @EnvWrapper
    public void wrap(AliyunLogMemberVO.LogMember logMember, IExtend iExtend) {
        if (!iExtend.getExtend()) return;
        ServerGroup serverGroup = serverGroupService.getById(logMember.getServerGroupId());
        logMember.setServerGroup(serverGroup);
        List<Server> servers = acqMemberServers(logMember);
        AliyunLog aliyunLog = aliyunLogService.getById(logMember.getAliyunLogId());
        logMember.setLog(BeanCopierUtil.copyProperties(aliyunLog, AliyunLogVO.Log.class));
        logMember.setMachineList(Lists.newArrayList(servers.stream().map(Server::getPrivateIp).collect(Collectors.toList())));
        AgoUtil.wrap(logMember);
    }

    private List<Server> acqMemberServers(AliyunLogMemberVO.LogMember logMember) {
        List<Server> servers;
        if (logMember.getEnvType() == 0) {
            servers = serverService.queryByServerGroupId(logMember.getServerGroupId());
        } else {
            servers = serverService.queryByGroupIdAndEnvType(logMember.getServerGroupId(), logMember.getEnvType());
        }
        return servers.stream().filter(Server::getIsActive).collect(Collectors.toList());
    }

}

