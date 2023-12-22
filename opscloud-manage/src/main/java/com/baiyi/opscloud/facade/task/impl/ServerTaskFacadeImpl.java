package com.baiyi.opscloud.facade.task.impl;

import com.baiyi.opscloud.common.base.ServerTaskStatusEnum;
import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.datasource.ansible.builder.AnsiblePlaybookArgumentsBuilder;
import com.baiyi.opscloud.datasource.ansible.builder.args.AnsiblePlaybookArgs;
import com.baiyi.opscloud.datasource.ansible.recorder.TaskLogStorehouse;
import com.baiyi.opscloud.datasource.ansible.task.AnsibleServerTask;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.domain.param.task.ServerTaskParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.task.ServerTaskVO;
import com.baiyi.opscloud.facade.task.ServerTaskFacade;
import com.baiyi.opscloud.facade.task.builder.ServerTaskBuilder;
import com.baiyi.opscloud.facade.task.builder.ServerTaskMemberBuilder;
import com.baiyi.opscloud.packer.task.ServerTaskPacker;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import com.baiyi.opscloud.service.task.ServerTaskService;
import com.baiyi.opscloud.util.PlaybookUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:21 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerTaskFacadeImpl extends SimpleDsInstanceProvider implements ServerTaskFacade {

    @Resource
    private ServerTaskService serverTaskService;

    @Resource
    private ServerTaskMemberService serverTaskMemberService;

    @Resource
    private TaskLogStorehouse taskLogStorehouse;

    @Resource
    private AnsiblePlaybookService ansiblePlaybookService;

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    private ServerTaskPacker serverTaskPacker;

    private static final int MAX_EXECUTING = 10;

    @Override
    public DataTable<ServerTaskVO.ServerTask> queryServerTaskPage(ServerTaskParam.ServerTaskPageQuery pageQuery) {
        DataTable<ServerTask> table = serverTaskService.queryServerTaskPage(pageQuery);
        List<ServerTaskVO.ServerTask> data = BeanCopierUtil.copyListProperties(table.getData(), ServerTaskVO.ServerTask.class)
                .stream()
                .peek(e -> serverTaskPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    @Async
    public void submitServerTask(ServerTaskParam.SubmitServerTask submitServerTask, String username) {
        ServerTask serverTask = ServerTaskBuilder.newBuilder(submitServerTask, username);
        serverTaskService.add(serverTask);
        List<ServerTaskMember> members = record(serverTask, submitServerTask.getServers());
        executeServerTask(serverTask, members);
    }

    /**
     * 录入服务器列表信息
     *
     * @param serverTask
     * @param servers
     */
    private List<ServerTaskMember> record(ServerTask serverTask, List<ServerVO.Server> servers) {
        if (CollectionUtils.isEmpty(servers)) {
            throw new OCException("服务器列表为空！");
        }
        List<ServerTaskMember> members = Lists.newArrayList();
        servers.forEach(server -> {
            ServerTaskMember member = ServerTaskMemberBuilder.newBuilder(serverTask, server);
            member.setOutputMsg(taskLogStorehouse.buildOutputLogPath(serverTask.getTaskUuid(), member));
            member.setErrorMsg(taskLogStorehouse.buildErrorLogPath(serverTask.getTaskUuid(), member));
            serverTaskMemberService.add(member);
            members.add(member);
        });
        return members;
    }

    private void executeServerTask(ServerTask serverTask, List<ServerTaskMember> members) {
        // 构建上下文
        DsInstanceContext instanceContext = buildDsInstanceContext(serverTask.getInstanceUuid());
        AnsibleConfig.Ansible ansible =
                dsConfigManager.build(instanceContext.getDsConfig(), AnsibleConfig.class).getAnsible();
        AnsiblePlaybook ansiblePlaybook = ansiblePlaybookService.getById(serverTask.getAnsiblePlaybookId());

        AnsiblePlaybookArgs args = AnsiblePlaybookArgs.builder()
                .extraVars(YamlUtil.loadVars(serverTask.getVars()).getVars())
                .keyFile(SystemEnvUtil.renderEnvHome(ansible.getPrivateKey()))
                .playbook(PlaybookUtil.toPath(ansiblePlaybook))
                .inventory(SystemEnvUtil.renderEnvHome(ansible.getInventoryHost()))
                .build();

        // ExecutorService fixedThreadPool = Executors.newFixedThreadPool(MAX_EXECUTING);
        // 使用迭代器遍历并执行所有服务器任务
        Iterator<ServerTaskMember> iter = members.iterator();

        while (iter.hasNext()) {
            // 查询当前执行中的任务是否达到最大并发
            if (serverTaskMemberService.countByTaskStatus(serverTask.getId(), ServerTaskStatusEnum.EXECUTING.name()) < MAX_EXECUTING) {
                ServerTaskMember serverTaskMember = iter.next();
                iter.remove();
                args.setHosts(serverTaskMember.getManageIp());
                CommandLine commandLine = AnsiblePlaybookArgumentsBuilder.build(ansible, args);
                AnsibleServerTask ansibleServerTask = new AnsibleServerTask(serverTask.getTaskUuid(),
                        serverTaskMember,
                        commandLine,
                        serverTaskMemberService,
                        taskLogStorehouse);
                // 执行任务
                // JDK21 VirtualThreads
                Thread.ofVirtual().start(ansibleServerTask);
            }
            NewTimeUtil.sleep(5L);
        }

        traceEndOfTask(serverTask);
    }

    /**
     * 追踪任务直到完成
     *
     * @param serverTask
     */
    private void traceEndOfTask(ServerTask serverTask) {
        while (true) {
            if (serverTask.getMemberSize() == serverTaskMemberService.countByFinalized(serverTask.getId(), true)) {
                serverTask.setFinalized(true);
                serverTask.setEndTime(new Date());
                serverTaskService.update(serverTask);
                // 任务完成
                return;
            } else {
                NewTimeUtil.sleep(1L);
            }
        }
    }

}