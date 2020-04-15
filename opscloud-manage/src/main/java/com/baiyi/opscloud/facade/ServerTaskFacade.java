package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
public interface ServerTaskFacade {

    DataTable<OcAnsiblePlaybookVO.AnsiblePlaybook> queryPlaybookPage(AnsiblePlaybookParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addPlaybook(OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook);

    BusinessWrapper<Boolean> updatePlaybook(OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook);

    BusinessWrapper<Boolean> deletePlaybookById(int id);

    BusinessWrapper<Boolean> executorCommand(ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor);

    BusinessWrapper<Boolean> executorPlaybook(ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor);

    /**
     * 查询playbook剧本路径
     * @param ocAnsiblePlaybook
     * @return
     */
    String getPlaybookPath(OcAnsiblePlaybook ocAnsiblePlaybook);

    OcServerTaskVO.ServerTask queryServerTaskByTaskId(int taskId);

    BusinessWrapper<Boolean> createAnsibleHosts();

    BusinessWrapper<Boolean> abortServerTask(int taskId);

    BusinessWrapper<Boolean> abortServerTaskMember(int memberId);
}
