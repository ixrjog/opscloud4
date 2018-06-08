package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.ansibleTask.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.config.ConfigFileCopyDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.task.CmdVO;
import com.sdg.cmdb.plugin.chain.TaskItem;

import java.util.List;


/**
 * Created by liangjian on 2017/3/31.
 */


public interface AnsibleTaskService {

    String task(boolean isSudo, String hostgroupName, String cmd);

    /**
     * 文件复制模块封装
     * @param isSudo
     * @param hostgroupName
     * @param configFileCopyDO
     * @return
     */
    String taskCopy(boolean isSudo, String hostgroupName, ConfigFileCopyDO configFileCopyDO);

    BusinessWrapper<Boolean> cmdTask(CmdVO cmdVO);

    BusinessWrapper<Boolean> scriptTask(CmdVO cmdVO);

    BusinessWrapper<Boolean> scriptTaskUpdateGetway();

    String taskScript(boolean isSudo, String hostgroupName, String cmd);

    BusinessWrapper<Boolean> doFileCopy(long id);

    /**
     * 按文件组
     * @param groupName
     * @return
     */
    BusinessWrapper<Boolean> doFileCopyByFileGroupName(String groupName);

    BusinessWrapper<Boolean> taskQuery(long taskId);

    String taskLogCleanup(ServerDO serverDO, int history);



    BusinessWrapper<Boolean>  taskGetwayAddAccount(String username, String pwd);

    BusinessWrapper<Boolean>  taskGetwayDelAccount(String username);


    TableVO<List<TaskScriptVO>> getTaskScriptPage(String scriptName, int sysScript, int page, int length);


    TaskScriptDO saveTaskScript(TaskScriptDO taskScriptDO);

    AnsibleVersionInfo acqAnsibleVersion();


    TableVO<List<AnsibleTaskVO>> getAnsibleTaskPage(String cmd, int page, int length);

}
