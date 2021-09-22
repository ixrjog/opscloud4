package com.baiyi.opscloud.facade.task.impl;

import com.baiyi.opscloud.ansible.args.PlaybookArgs;
import com.baiyi.opscloud.ansible.builder.AnsiblePlaybookArgsBuilder;
import com.baiyi.opscloud.ansible.util.AnsibleUtil;
import com.baiyi.opscloud.common.datasource.AnsibleDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAnsibleConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.datasource.util.SystemEnvUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.domain.param.task.ServerTaskParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.task.ServerTaskFacade;
import com.baiyi.opscloud.facade.task.builder.ServerTaskBuilder;
import com.baiyi.opscloud.facade.task.builder.ServerTaskMemberBuilder;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import com.baiyi.opscloud.service.task.ServerTaskService;
import com.baiyi.opscloud.util.PlaybookUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

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
    private AnsiblePlaybookService ansiblePlaybookService;

    @Resource
    private DsConfigFactory dsConfigFactory;

    public void submitServerTask(ServerTaskParam.SubmitServerTask submitServerTask) {
        ServerTask serverTask = ServerTaskBuilder.newBuilder(submitServerTask);
        serverTaskService.add(serverTask);
        List<ServerTaskMember> members = record(serverTask, submitServerTask.getServers());
        execute(serverTask,members);
    }

    /**
     * 录入服务器列表信息
     *
     * @param serverTask
     * @param servers
     */
    private List<ServerTaskMember> record(ServerTask serverTask, List<ServerVO.Server> servers) {
        if (CollectionUtils.isEmpty(servers)) throw new CommonRuntimeException("服务器列表为空！");
        List<ServerTaskMember> members = Lists.newArrayList();
        servers.forEach(s -> {
            ServerTaskMember member = ServerTaskMemberBuilder.newBuilder(serverTask, s);
            serverTaskMemberService.add(member);
            members.add(member);
        });
        return members;
    }

    private void execute(ServerTask serverTask, List<ServerTaskMember> members){
        DsInstanceContext instanceContext = buildDsInstanceContext(serverTask.getInstanceUuid());
        DsAnsibleConfig.Ansible ansible = dsConfigFactory.build(instanceContext.getDsConfig(), AnsibleDsInstanceConfig.class).getAnsible();
        AnsiblePlaybook ansiblePlaybook = ansiblePlaybookService.getById(serverTask.getAnsiblePlaybookId());

        PlaybookArgs args = PlaybookArgs.builder()
                .extraVars(AnsibleUtil.toVars(serverTask.getVars()).getVars())
                .keyFile(SystemEnvUtil.renderEnvHome(ansible.getPrivateKey()))
                .playbook( PlaybookUtil.toPath(ansiblePlaybook))
                .inventory(SystemEnvUtil.renderEnvHome(ansible.getInventoryHost()))
                .build();


        CommandLine commandLine = AnsiblePlaybookArgsBuilder.build(ansible, args);

        // 使用迭代器
        Iterator<ServerTaskMember> iter = members.iterator();
        while (iter.hasNext()) {
//            if (priority > Integer.valueOf(iter.next().getPriority()))
//                iter.remove();

        }
    }


}
