package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.param.ansible.AnsibleScriptParam;
import com.baiyi.opscloud.domain.param.ansible.ServerTaskHistoryParam;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.ansible.AnsibleScriptVO;
import com.baiyi.opscloud.domain.vo.ansible.AnsibleVersionVO;
import com.baiyi.opscloud.domain.vo.preview.PreviewFileVO;
import com.baiyi.opscloud.domain.vo.server.ServerTaskVO;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
public interface ServerTaskFacade {

    DataTable<ServerTaskVO.ServerTask> queryTaskHistoryPage(@Valid ServerTaskHistoryParam.PageQuery pageQuery);

    DataTable<AnsiblePlaybookVO.AnsiblePlaybook> queryPlaybookPage(AnsiblePlaybookParam.PageQuery pageQuery);

    DataTable<AnsibleScriptVO.AnsibleScript> queryScriptPage(AnsibleScriptParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addScript(AnsibleScriptVO.AnsibleScript ansibleScript);

    BusinessWrapper<Boolean> updateScript(AnsibleScriptVO.AnsibleScript ansibleScript);

    BusinessWrapper<Boolean> deleteScriptById(int id);

    BusinessWrapper<Boolean> addPlaybook(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook);

    BusinessWrapper<Boolean> updatePlaybook(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook);

    BusinessWrapper<Boolean> deletePlaybookById(int id);

    BusinessWrapper<OcServerTask> executorCommand(ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor);

    BusinessWrapper<OcServerTask> executorScript(ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor);

    BusinessWrapper<OcServerTask> executorPlaybook(ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor);

    ServerTaskVO.ServerTask queryServerTaskByTaskId(int taskId);

    BusinessWrapper<Boolean> createAnsibleHosts();

    BusinessWrapper<Boolean> abortServerTask(int taskId);

    BusinessWrapper<Boolean> abortServerTaskMember(int memberId);

    BusinessWrapper<AnsibleVersionVO.AnsibleVersion> queryAnsibleVersion();

    BusinessWrapper<PreviewFileVO> previewAnsibleHosts();
}
