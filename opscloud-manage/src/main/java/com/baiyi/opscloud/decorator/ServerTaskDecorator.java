package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcServerTaskMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.vo.env.OcEnvVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskMemberVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/9 12:50 下午
 * @Version 1.0
 */
@Component
public class ServerTaskDecorator {

    @Resource
    private OcServerTaskMemberService ocServerTaskMemberService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcEnvService ocEnvService;

    public OcServerTaskVO.ServerTask decorator(OcServerTaskVO.ServerTask serverTask) {
        List<OcServerTaskMember> memberList = ocServerTaskMemberService.queryOcServerTaskMemberByTaskId(serverTask.getId());

        serverTask.setMemberMap(getMemberMap(memberList));
        return serverTask;
    }

    private Map<String, List<OcServerTaskMemberVO.ServerTaskMember>> getMemberMap(List<OcServerTaskMember> memberList) {
        Map<String, List<OcServerTaskMemberVO.ServerTaskMember>> memberMap = Maps.newHashMap();
        for (OcServerTaskMember member : memberList)
            if (memberMap.containsKey(member.getTaskStatus())) {
                memberMap.get(member.getTaskStatus()).add(decorator(member));
            } else {
                List<OcServerTaskMemberVO.ServerTaskMember> members = Lists.newArrayList(decorator(member));
                memberMap.put(member.getTaskStatus(), members);
            }
        return memberMap;
    }

    private OcServerTaskMemberVO.ServerTaskMember decorator(OcServerTaskMember member) {
        OcServerTaskMemberVO.ServerTaskMember serverTaskMember = BeanCopierUtils.copyProperties(member, OcServerTaskMemberVO.ServerTaskMember.class);
        OcServer ocServer = ocServerService.queryOcServerById(serverTaskMember.getServerId());
        if (ocServer == null) return serverTaskMember;
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        serverTaskMember.setEnv(BeanCopierUtils.copyProperties(ocEnv, OcEnvVO.Env.class));
        return serverTaskMember;
    }

}
