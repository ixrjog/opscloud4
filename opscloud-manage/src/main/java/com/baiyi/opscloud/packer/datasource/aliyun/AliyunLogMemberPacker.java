package com.baiyi.opscloud.packer.datasource.aliyun;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLog;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLogMember;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;
import com.baiyi.opscloud.packer.base.IPacker;
import com.baiyi.opscloud.packer.sys.EnvPacker;
import com.baiyi.opscloud.service.datasource.AliyunLogService;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.util.time.AgoUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/17 11:13 上午
 * @Version 1.0
 */
@Component
public class AliyunLogMemberPacker implements IPacker<AliyunLogMemberVO.LogMember, AliyunLogMember> {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private AliyunLogService aliyunLogService;

    @Resource
    private ServerService serverService;

    @Resource
    private EnvPacker envPacker;

    public AliyunLogMemberVO.LogMember toVO(AliyunLogMember aliyunLogMember) {
        return BeanCopierUtil.copyProperties(aliyunLogMember, AliyunLogMemberVO.LogMember.class);
    }

    public AliyunLogMemberVO.LogMember wrap(AliyunLogMember aliyunLogMember, IExtend iExtend) {
        AliyunLogMemberVO.LogMember vo = toVO(aliyunLogMember);
        if (iExtend.getExtend()) {
            ServerGroup serverGroup = serverGroupService.getById(aliyunLogMember.getServerGroupId());
            vo.setServerGroup(serverGroup);
            List<Server> servers = acqMemberServers(aliyunLogMember);
            AliyunLog aliyunLog = aliyunLogService.getById(aliyunLogMember.getAliyunLogId());
            vo.setLog(BeanCopierUtil.copyProperties(aliyunLog, AliyunLogVO.Log.class));
            vo.setMachineList(Lists.newArrayList(servers.stream().map(Server::getPrivateIp).collect(Collectors.toList())));
            AgoUtil.wrap(vo);
            envPacker.wrap(vo);
        }
        return vo;
    }


    private List<Server> acqMemberServers(AliyunLogMember aliyunLogMember) {
        List<Server> servers;
        if (aliyunLogMember.getEnvType() == 0) {
            servers = serverService.queryByServerGroupId(aliyunLogMember.getServerGroupId());
        } else {
            servers = serverService.queryByGroupIdAndEnvType(aliyunLogMember.getServerGroupId(), aliyunLogMember.getEnvType());
        }
        return servers.stream().filter(Server::getIsActive).collect(Collectors.toList());
    }


}

