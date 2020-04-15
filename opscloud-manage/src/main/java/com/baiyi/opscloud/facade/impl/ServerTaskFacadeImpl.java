package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.ansible.handler.AnsibleTaskHandler;
import com.baiyi.opscloud.builder.ServerTaskBuilder;
import com.baiyi.opscloud.common.base.ServerTaskStopType;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.RedisKeyUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.AnsiblePlaybookDecorator;
import com.baiyi.opscloud.decorator.ServerTaskDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.generator.OcServerTask;
import com.baiyi.opscloud.domain.generator.OcServerTaskMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;
import com.baiyi.opscloud.facade.AttributeFacade;
import com.baiyi.opscloud.facade.ServerTaskFacade;
import com.baiyi.opscloud.service.ansible.OcAnsiblePlaybookService;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import com.baiyi.opscloud.service.server.OcServerTaskService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
@Service
public class ServerTaskFacadeImpl implements ServerTaskFacade {

    @Resource
    private AnsibleTaskHandler ansibleTaskHandler;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcServerTaskService ocServerTaskService;

    @Resource
    private OcServerTaskMemberService ocServerTaskMemberService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ServerTaskDecorator serverTaskDecorator;

    @Resource
    private AttributeFacade attributeFacade;

    @Resource
    private OcAnsiblePlaybookService ocAnsiblePlaybookService;

    @Resource
    private AnsibleConfig ansibleConfig;

    @Resource
    private AnsiblePlaybookDecorator ansiblePlaybookDecorator;

    @Override
    public DataTable<OcAnsiblePlaybookVO.AnsiblePlaybook> queryPlaybookPage(AnsiblePlaybookParam.PageQuery pageQuery) {
        DataTable<OcAnsiblePlaybook> table = ocAnsiblePlaybookService.queryOcAnsiblePlaybookByParam(pageQuery);
        DataTable<OcAnsiblePlaybookVO.AnsiblePlaybook> dataTable
                = new DataTable<>(table.getData().stream().map(e -> ansiblePlaybookDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addPlaybook(OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        OcAnsiblePlaybook ocAnsiblePlaybook = BeanCopierUtils.copyProperties(ansiblePlaybook, OcAnsiblePlaybook.class);
        ocAnsiblePlaybookService.addOcAnsiblePlaybook(ocAnsiblePlaybook);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updatePlaybook(OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        OcAnsiblePlaybook ocAnsiblePlaybook = BeanCopierUtils.copyProperties(ansiblePlaybook, OcAnsiblePlaybook.class);
        ocAnsiblePlaybookService.updateOcAnsiblePlaybook(ocAnsiblePlaybook);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deletePlaybookById(int id) {
        ocAnsiblePlaybookService.deleteOcAnsiblePlaybookById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> executorCommand(ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor) {
        // serverTaskCommandExecutor.setBecomeUser("root");
        OcUser ocUser = ocUserService.queryOcUserByUsername(SessionUtils.getUsername());
        String key = RedisKeyUtils.getMyServerTreeKey(ocUser.getId(), serverTaskCommandExecutor.getUuid());
        if (!redisUtil.hasKey(key))
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_TREE_NOT_EXIST);
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) redisUtil.get(key);
        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskCommandExecutor);
        ocServerTaskService.addOcServerTask(ocServerTask);
        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskCommandExecutor);
        BusinessWrapper wrapper = BusinessWrapper.SUCCESS;
        wrapper.setBody(ocServerTask);
        return wrapper;
    }

    @Override
    public BusinessWrapper<Boolean> executorPlaybook(ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(SessionUtils.getUsername());
        String key = RedisKeyUtils.getMyServerTreeKey(ocUser.getId(), serverTaskPlaybookExecutor.getUuid());
        if (!redisUtil.hasKey(key))
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_TREE_NOT_EXIST);
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) redisUtil.get(key);
        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskPlaybookExecutor);
        ocServerTaskService.addOcServerTask(ocServerTask);
        // 写入playbook
        OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(serverTaskPlaybookExecutor.getPlaybookId());
        String playbookPath = getPlaybookPath(ocAnsiblePlaybook);
        IOUtils.writeFile(ocAnsiblePlaybook.getPlaybook(), playbookPath);

        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskPlaybookExecutor, playbookPath);
        BusinessWrapper wrapper = BusinessWrapper.SUCCESS;
        wrapper.setBody(ocServerTask);
        return wrapper;
    }

    @Override
    public String getPlaybookPath(OcAnsiblePlaybook ocAnsiblePlaybook) {
        return Joiner.on("/").join(ansibleConfig.getDataPath(), "playbook", ocAnsiblePlaybook.getPlaybookUuid() + ".yml");
    }

    @Override
    public OcServerTaskVO.ServerTask queryServerTaskByTaskId(int taskId) {
        OcServerTask ocServerTask = ocServerTaskService.queryOcServerTaskById(taskId);
        OcServerTaskVO.ServerTask serverTask = BeanCopierUtils.copyProperties(ocServerTask, OcServerTaskVO.ServerTask.class);
        return serverTaskDecorator.decorator(serverTask);
    }

    @Override
    public BusinessWrapper<Boolean> createAnsibleHosts() {
        attributeFacade.createAnsibleHosts();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> abortServerTask(int taskId) {
        OcServerTask ocServerTask = ocServerTaskService.queryOcServerTaskById(taskId);
        if (ocServerTask == null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_NOT_EXIST);
        if (ocServerTask.getFinalized() == 1)
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_HAS_FINALIIZED_AND_CANNOT_BE_MODIFIED);
        ocServerTask.setFinalized(1);
        ocServerTask.setStopType(ServerTaskStopType.SERVER_TASK_STOP.getType());
        ocServerTaskService.updateOcServerTask(ocServerTask);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> abortServerTaskMember(int memberId) {
        OcServerTaskMember ocServerTaskMember = ocServerTaskMemberService.queryOcServerTaskMemberById(memberId);
        if (ocServerTaskMember == null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_MEMBER_NOT_EXIST);
        if (ocServerTaskMember.getFinalized() == 1)
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_HAS_FINALIIZED_AND_CANNOT_BE_MODIFIED);
        ocServerTaskMember.setStopType(ServerTaskStopType.MEMBER_TASK_STOP.getType());
        ocServerTaskMember.setFinalized(1);
        ocServerTaskMemberService.updateOcServerTaskMember(ocServerTaskMember);
        return BusinessWrapper.SUCCESS;
    }

}
