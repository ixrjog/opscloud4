package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.ansible.bo.MemberExecutorLogBO;
import com.baiyi.opscloud.ansible.handler.TaskLogRecorder;
import com.baiyi.opscloud.common.util.AnsibleUtils;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.domain.generator.OcServerTaskMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.env.OcEnvVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskMemberVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    @Resource
    private TaskLogRecorder taskLogRecorder;

    public OcServerTaskVO.ServerTask decorator(OcServerTaskVO.ServerTask serverTask) {
        List<OcServerTaskMember> memberList = ocServerTaskMemberService.queryOcServerTaskMemberByTaskId(serverTask.getId());
        invokeMemberList(serverTask, memberList);
        try {
            switch (serverTask.getTaskType()) {
                case 0: // command
                    ServerTaskExecutorParam.ServerTaskCommandExecutor commandParam
                            = new GsonBuilder().create().fromJson(serverTask.getExecutorParam(), ServerTaskExecutorParam.ServerTaskCommandExecutor.class);
                    serverTask.setExecutorParamDetail(commandParam);
                    break;
                case 2: // playbook
                    ServerTaskExecutorParam.ServerTaskPlaybookExecutor playbookParam
                            = new GsonBuilder().create().fromJson(serverTask.getExecutorParam(), ServerTaskExecutorParam.ServerTaskPlaybookExecutor.class);
                    serverTask.setExecutorParamDetail(playbookParam);
                    break;
                default:
            }
        } catch (JsonSyntaxException e) {
        }
        return serverTask;
    }

    private void invokeMemberList(OcServerTaskVO.ServerTask serverTask, List<OcServerTaskMember> memberList) {
        Map<String, List<OcServerTaskMemberVO.ServerTaskMember>> memberMap = Maps.newHashMap();

        OcServerTaskVO.ServerTastStatistics taskStatistics = new OcServerTaskVO.ServerTastStatistics();

        int successfulCount = 0;
        int failedCount = 0;
        int errorCount = 0;

        for (OcServerTaskMember member : memberList) {
            if (memberMap.containsKey(member.getTaskStatus())) {
                memberMap.get(member.getTaskStatus()).add(decorator(member));
            } else {
                List<OcServerTaskMemberVO.ServerTaskMember> members = Lists.newArrayList(decorator(member));
                memberMap.put(member.getTaskStatus(), members);
            }
            if (member.getFinalized() == 0) continue;
            if (member.getTaskResult() == null)
                continue;
            switch (member.getTaskResult()) {
                case "SUCCESSFUL":
                    successfulCount += 1;
                    break;
                case "FAILED":
                    failedCount += 1;
                    break;
                default:
                    errorCount += 1;
                    break;
            }
        }
        taskStatistics.setTotal(memberList.size());
        taskStatistics.setSuccessfulCount(successfulCount);
        taskStatistics.setFailedCount(failedCount);
        taskStatistics.setErrorCount(errorCount);
        serverTask.setMemberMap(memberMap);
        serverTask.setTaskStatistics(taskStatistics);
    }

    private OcServerTaskMemberVO.ServerTaskMember decorator(OcServerTaskMember member) {
        OcServerTaskMemberVO.ServerTaskMember serverTaskMember = BeanCopierUtils.copyProperties(member, OcServerTaskMemberVO.ServerTaskMember.class);
        serverTaskMember.setHide(false);
        OcServer ocServer = ocServerService.queryOcServerById(serverTaskMember.getServerId());
        if (ocServer == null) return serverTaskMember;
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        serverTaskMember.setEnv(BeanCopierUtils.copyProperties(ocEnv, OcEnvVO.Env.class));
        serverTaskMember.setSuccess(serverTaskMember.getExitValue() != null && serverTaskMember.getExitValue() == 0);
        if (serverTaskMember.getFinalized() == 1) {
            if (!StringUtils.isEmpty(member.getOutputMsg()))
                serverTaskMember.setOutputMsgLog(IOUtils.readFile(member.getOutputMsg()));
            if (!StringUtils.isEmpty(member.getErrorMsg()))
                serverTaskMember.setErrorMsgLog(IOUtils.readFile(member.getErrorMsg()));
        } else {
            MemberExecutorLogBO memberExecutorLogBO = taskLogRecorder.getLog(member.getId());
            if (memberExecutorLogBO != null) {
                if (!StringUtils.isEmpty(memberExecutorLogBO.getOutputMsg()))
                    serverTaskMember.setOutputMsgLog(memberExecutorLogBO.getOutputMsg());
                if (!StringUtils.isEmpty(memberExecutorLogBO.getErrorMsg()))
                    serverTaskMember.setErrorMsgLog(memberExecutorLogBO.getErrorMsg());
            }
        }
        if (serverTaskMember.getSuccess()) {
            // 格式化数据
            String resultHead = AnsibleUtils.getResultHead(serverTaskMember.getOutputMsgLog());
            if (!StringUtils.isEmpty(resultHead)) {
                String resultStr = serverTaskMember.getOutputMsgLog().replace(resultHead, "");
                try {
                    OcServerTaskMemberVO.AnsibleResult ansibleResult = new Gson().fromJson(resultStr, OcServerTaskMemberVO.AnsibleResult.class);
                    serverTaskMember.setResult(ansibleResult);
                } catch (Exception e) {
                }
            }
        }
        return serverTaskMember;
    }

}
