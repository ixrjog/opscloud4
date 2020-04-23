package com.baiyi.opscloud.facade.impl;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.ansible.bo.TaskResult;
import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.ansible.factory.ExecutorFactory;
import com.baiyi.opscloud.ansible.handler.AnsibleTaskHandler;
import com.baiyi.opscloud.ansible.impl.AnsibleCommandExecutor;
import com.baiyi.opscloud.ansible.impl.AnsiblePlaybookExecutor;
import com.baiyi.opscloud.ansible.impl.AnsibleScriptExecutor;
import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.base.ServerTaskStopType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.decorator.AnsiblePlaybookDecorator;
import com.baiyi.opscloud.decorator.AnsibleScriptDecorator;
import com.baiyi.opscloud.decorator.ServerTaskDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsibleScript;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTaskMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.param.ansible.AnsibleScriptParam;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsibleVersionVO;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsibleScriptVO;
import com.baiyi.opscloud.domain.vo.preview.PreviewFileVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;
import com.baiyi.opscloud.facade.AttributeFacade;
import com.baiyi.opscloud.facade.ServerTaskFacade;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.service.ansible.OcAnsiblePlaybookService;
import com.baiyi.opscloud.service.ansible.OcAnsibleScriptService;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import com.baiyi.opscloud.service.server.OcServerTaskService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
@Service
public class ServerTaskFacadeImpl implements ServerTaskFacade {

    @Resource
    private OcServerTaskService ocServerTaskService;

    @Resource
    private UserFacade userFacade;

    @Resource
    private OcServerTaskMemberService ocServerTaskMemberService;

    @Resource
    private ServerTaskDecorator serverTaskDecorator;

    @Resource
    private AttributeFacade attributeFacade;

    @Resource
    private OcAnsiblePlaybookService ocAnsiblePlaybookService;

    @Resource
    private OcAnsibleScriptService ocAnsibleScriptService;

    @Resource
    private AnsiblePlaybookDecorator ansiblePlaybookDecorator;

    @Resource
    private AnsibleScriptDecorator ansibleScriptDecorator;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private AnsibleTaskHandler ansibleTaskHandler;

    @Resource
    private AnsibleConfig ansibleConfig;

    @Override
    public DataTable<OcAnsiblePlaybookVO.AnsiblePlaybook> queryPlaybookPage(AnsiblePlaybookParam.PageQuery pageQuery) {
        DataTable<OcAnsiblePlaybook> table = ocAnsiblePlaybookService.queryOcAnsiblePlaybookByParam(pageQuery);
        DataTable<OcAnsiblePlaybookVO.AnsiblePlaybook> dataTable
                = new DataTable<>(table.getData().stream().map(e -> ansiblePlaybookDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<OcAnsibleScriptVO.AnsibleScript> queryScriptPage(AnsibleScriptParam.PageQuery pageQuery) {
        DataTable<OcAnsibleScript> table = ocAnsibleScriptService.queryOcAnsibleScriptByParam(pageQuery);
        DataTable<OcAnsibleScriptVO.AnsibleScript> dataTable
                = new DataTable<>(table.getData().stream().map(e -> ansibleScriptDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addScript(OcAnsibleScriptVO.AnsibleScript ansibleScript) {
        OcAnsibleScript ocAnsibleScript = BeanCopierUtils.copyProperties(ansibleScript, OcAnsibleScript.class);
        ocAnsibleScript.setScriptUuid(UUIDUtils.getUUID());
        OcUser ocUser = userFacade.getOcUserBySession();
        ocUser.setPassword("");
        ocAnsibleScript.setUserId(ocUser.getId());
        ocAnsibleScript.setUserDetail(JSON.toJSONString(ocUser));
        ocAnsibleScriptService.addOcAnsibleScript(ocAnsibleScript);
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 鉴权
     *
     * @return
     */
    private BusinessWrapper<Boolean> checkAuth(OcAnsibleScript ocAnsibleScript) {
        // 无需鉴权
        if (ocAnsibleScript.getScriptLock() == 0)
            return BusinessWrapper.SUCCESS;
        OcUser ocUser = userFacade.getOcUserBySession();
        return userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.OPS.getLevel());
    }

    @Override
    public BusinessWrapper<Boolean> updateScript(OcAnsibleScriptVO.AnsibleScript ansibleScript) {
        OcAnsibleScript preAnsibleScript = ocAnsibleScriptService.queryOcAnsibleScriptById(ansibleScript.getId());
        BusinessWrapper<Boolean> wrapper = checkAuth(preAnsibleScript);
        if (!wrapper.isSuccess())
            return wrapper;
        preAnsibleScript.setName(ansibleScript.getName());
        preAnsibleScript.setScriptLang(ansibleScript.getScriptLang());
        preAnsibleScript.setComment(ansibleScript.getComment());
        preAnsibleScript.setScriptLock(ansibleScript.getScriptLock());
        preAnsibleScript.setScriptContent(ansibleScript.getScriptContent());
        ocAnsibleScriptService.updateOcAnsibleScript(preAnsibleScript);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteScriptById(int id) {
        OcAnsibleScript preAnsibleScript = ocAnsibleScriptService.queryOcAnsibleScriptById(id);
        BusinessWrapper<Boolean> wrapper = checkAuth(preAnsibleScript);
        if (!wrapper.isSuccess())
            return wrapper;
        ocAnsibleScriptService.deleteOcAnsibleScriptById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addPlaybook(OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        OcAnsiblePlaybook ocAnsiblePlaybook = BeanCopierUtils.copyProperties(ansiblePlaybook, OcAnsiblePlaybook.class);
        ocAnsiblePlaybook.setPlaybookUuid(UUIDUtils.getUUID());
        OcUser ocUser = userFacade.getOcUserBySession();
        ocUser.setPassword("");
        ocAnsiblePlaybook.setUserId(ocUser.getId());
        ocAnsiblePlaybook.setUserDetail(JSON.toJSONString(ocUser));
        ocAnsiblePlaybookService.addOcAnsiblePlaybook(ocAnsiblePlaybook);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updatePlaybook(OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(ansiblePlaybook.getId());

        ocAnsiblePlaybook.setComment(ansiblePlaybook.getComment());
        ocAnsiblePlaybook.setName(ansiblePlaybook.getName());
        ocAnsiblePlaybook.setPlaybook(ansiblePlaybook.getPlaybook());
        ocAnsiblePlaybook.setExtraVars(ansiblePlaybook.getExtraVars());
        ocAnsiblePlaybook.setTags(ansiblePlaybook.getTags());

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
        return ExecutorFactory.getAnsibleExecutorByKey(AnsibleCommandExecutor.COMPONENT_NAME).executorByParam(serverTaskCommandExecutor);
    }

    @Override
    public BusinessWrapper<Boolean> executorScript(ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor) {
        return ExecutorFactory.getAnsibleExecutorByKey(AnsibleScriptExecutor.COMPONENT_NAME).executorByParam(serverTaskScriptExecutor);
    }

    @Override
    public BusinessWrapper<Boolean> executorPlaybook(ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor) {
        return ExecutorFactory.getAnsibleExecutorByKey(AnsiblePlaybookExecutor.COMPONENT_NAME).executorByParam(serverTaskPlaybookExecutor);
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

    @Override
    public BusinessWrapper<Boolean> queryAnsibleVersion() {
        AnsibleVersionVO.AnsibleVersion version = new AnsibleVersionVO.AnsibleVersion();
        try {
            TaskResult ansibleVersion = ansibleTaskHandler.getAnsibleVersion();
            version.setVersion(ansibleVersion.getOutputStream().toString("utf8"));
        } catch (UnsupportedEncodingException e) {

        }
        try {
            TaskResult playbookVersion = ansibleTaskHandler.getAnsiblePlaybookVersion();
            version.setPlaybookVersion(playbookVersion.getOutputStream().toString("utf8"));
        } catch (UnsupportedEncodingException e) {
        }
        BusinessWrapper wrapper = new BusinessWrapper(true);
        wrapper.setBody(version);
        return wrapper;
    }

    @Override
    public BusinessWrapper<Boolean> previewAnsibleHosts() {
        String path = Joiner.on("/").join(ansibleConfig.acqInventoryPath(), AnsibleConfig.ANSIBLE_HOSTS);
        String ansibleHosts = IOUtils.readFile(path);
        PreviewFileVO previewFile = PreviewFileVO.builder()
                .name(AnsibleConfig.ANSIBLE_HOSTS)
                .path(path)
                .content(ansibleHosts)
                .comment("Ansible主机配置文件")
                .build();
        BusinessWrapper wrapper = new BusinessWrapper(true);
        wrapper.setBody(previewFile);
        return wrapper;
    }

}
