package com.baiyi.opscloud.decorator.server;

import com.baiyi.opscloud.ansible.bo.MemberExecutorLogBO;
import com.baiyi.opscloud.ansible.handler.TaskLogRecorder;
import com.baiyi.opscloud.common.util.AnsibleUtils;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.TimeAgoUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTaskMember;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.server.ServerTaskMemberVO;
import com.baiyi.opscloud.domain.vo.server.ServerTaskVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;
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

    public ServerTaskVO.ServerTask decorator(OcServerTask ocServerTask) {
        ServerTaskVO.ServerTask serverTask = BeanCopierUtils.copyProperties(ocServerTask, ServerTaskVO.ServerTask.class);
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        serverTask.setServerTarget(new GsonBuilder().create().fromJson(serverTask.getServerTargetDetail(), type));
        // user
        serverTask.setUser(new GsonBuilder().create().fromJson(serverTask.getUserDetail(), UserVO.User.class));

        serverTask.setAgo(TimeAgoUtils.format(serverTask.getCreateTime()));
        return serverTask;
    }

    public ServerTaskVO.ServerTask decorator(ServerTaskVO.ServerTask serverTask) {
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

    private void invokeMemberList(ServerTaskVO.ServerTask serverTask, List<OcServerTaskMember> memberList) {
        Map<String, List<ServerTaskMemberVO.ServerTaskMember>> memberMap = Maps.newHashMap();

        ServerTaskVO.ServerTastStatistics taskStatistics = new ServerTaskVO.ServerTastStatistics();

        int successfulCount = 0;
        int failedCount = 0;
        int errorCount = 0;

        for (OcServerTaskMember member : memberList) {
            if (memberMap.containsKey(member.getTaskStatus())) {
                memberMap.get(member.getTaskStatus()).add(decorator(member));
            } else {
                List<ServerTaskMemberVO.ServerTaskMember> members = Lists.newArrayList(decorator(member));
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

    private ServerTaskMemberVO.ServerTaskMember decorator(OcServerTaskMember member) {
        ServerTaskMemberVO.ServerTaskMember serverTaskMember = BeanCopierUtils.copyProperties(member, ServerTaskMemberVO.ServerTaskMember.class);
        serverTaskMember.setHide(false);
        OcServer ocServer = ocServerService.queryOcServerById(serverTaskMember.getServerId());
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(member.getEnvType());
        serverTaskMember.setEnv(BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class));
        if (ocServer == null) return serverTaskMember;
        serverTaskMember.setSuccess(serverTaskMember.getExitValue() != null && serverTaskMember.getExitValue() == 0);
        serverTaskMember.setShowErrorLog(false); // 不显示错误日志
        if (serverTaskMember.getFinalized() == 1) {
            if (!StringUtils.isEmpty(member.getOutputMsg()))
                serverTaskMember.setOutputMsgLog(IOUtils.readFile(member.getOutputMsg()));
            if (!StringUtils.isEmpty(member.getErrorMsg())) {
                serverTaskMember.setErrorMsgLog(IOUtils.readFile(member.getErrorMsg()));
                if (serverTaskMember.getExitValue() != 0)
                    serverTaskMember.setShowErrorLog(true); // 显示错误日志
            }
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
                    ServerTaskMemberVO.AnsibleResult ansibleResult = new Gson().fromJson(resultStr, ServerTaskMemberVO.AnsibleResult.class);
                    serverTaskMember.setResult(ansibleResult);
                } catch (Exception e) {
                }
            }
        }
        return serverTaskMember;
    }

}
