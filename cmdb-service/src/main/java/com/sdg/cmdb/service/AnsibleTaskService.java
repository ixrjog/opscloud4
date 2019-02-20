package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.ansibleTask.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.config.ConfigFileCopyDO;
import com.sdg.cmdb.domain.copy.CopyVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.task.CmdVO;
import com.sdg.cmdb.domain.task.DoPlaybook;
import com.sdg.cmdb.plugin.chain.TaskItem;

import java.util.List;


/**
 * Created by liangjian on 2017/3/31.
 */


public interface AnsibleTaskService {

    String task(boolean isSudo, String hostgroupName, String cmd);

    BusinessWrapper<Boolean> cmdTask(CmdVO cmdVO);

    BusinessWrapper<Boolean> scriptTask(CmdVO cmdVO);

    /**
     * 对外Playbook执行接口
     * @param doPlaybook
     * @return
     */
    PlaybookTaskDO playbookTask(DoPlaybook doPlaybook);

    PlaybookTaskVO getPlaybookTask(long id);

    BusinessWrapper<Boolean> taskQuery(long taskId);

    String taskLogCleanup(ServerDO serverDO, int history);

    TableVO<List<TaskScriptVO>> getTaskScriptPage(String scriptName, int sysScript, int page, int length);

    List<TaskScriptDO> getTaskScriptPlaybook();

    List<TaskScriptDO> queryPlaybook(String playbookName);

    TaskScriptDO saveTaskScript(TaskScriptDO taskScriptDO);

    AnsibleVersionInfo acqAnsibleVersion();

    AnsibleVersionInfo acqAnsiblePlaybookVersion();

    TableVO<List<AnsibleTaskVO>> getAnsibleTaskPage(String cmd, int page, int length);

    TaskResult doPlaybook(boolean isSudo, String hostPattern, String playbook, String extraVars);

    void playbook(boolean isSudo, String hostPattern, String playbook, String extraVars,PlaybookLogDO playbookLogDO);

    String getPlaybookPath(TaskScriptDO taskScriptDO);

}
