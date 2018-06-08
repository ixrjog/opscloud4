package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ansibleTask.AnsibleTaskDO;

import com.sdg.cmdb.domain.ansibleTask.AnsibleTaskServerDO;
import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AnsibleTaskDao {

    int addAnsibleTask(AnsibleTaskDO ansibleTaskDO);

    AnsibleTaskDO getAnsibleTask(@Param("id") long id);

    int updateAnsibleTask(AnsibleTaskDO ansibleTaskDO);

    int addAnsibleTaskServer(AnsibleTaskServerDO ansibleTaskServerDO);

    int cntAnsibleTaskServerByTaskId(@Param("ansibleTaskId") long ansibleTaskId);

    List<AnsibleTaskServerDO> queryAnsibleTaskServerByTaskId(@Param("ansibleTaskId") long ansibleTaskId);

    /**
     * taskScript详情页
     *
     * @param scriptName
     * @param userId
     * @param sysScript
     * @return
     */
    List<TaskScriptDO> queryTaskScriptPage(@Param("scriptName") String scriptName,
                                           @Param("userId") long userId,
                                           @Param("sysScript") int sysScript,
                                           @Param("pageStart") long pageStart, @Param("length") int length);

    long getTaskScriptSize(@Param("scriptName") String scriptName,
                           @Param("userId") long userId,
                           @Param("sysScript") int sysScript);

    List<TaskScriptDO> queryTaskScriptPageByAdmin(@Param("scriptName") String scriptName,
                                           @Param("sysScript") int sysScript,
                                           @Param("pageStart") long pageStart, @Param("length") int length);

    long getTaskScriptSizeByAdmin(@Param("scriptName") String scriptName,
                           @Param("sysScript") int sysScript);

    TaskScriptDO getTaskScript(@Param("id") long id);

    TaskScriptDO getTaskScriptByScriptName(@Param("scriptName") String scriptName);

    int addTaskScript(TaskScriptDO taskScriptDO);

    int updateTaskScript(TaskScriptDO taskScriptDO);


    List<AnsibleTaskDO> queryAnsibleTaskPage(@Param("cmd") String cmd,
                                             @Param("pageStart") long pageStart, @Param("length") int length);

    long getAnsibleTaskSize(@Param("cmd") String cmd);

}
