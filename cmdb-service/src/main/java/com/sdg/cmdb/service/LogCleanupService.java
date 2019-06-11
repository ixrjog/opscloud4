package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ansibleTask.AnsibleTaskServerDO;
import com.sdg.cmdb.domain.logCleanup.LogcleanupDO;
import com.sdg.cmdb.domain.logCleanup.LogcleanupVO;

import java.util.List;

public interface LogcleanupService {

    TableVO<List<LogcleanupVO>> getLogcleanupPage(long serverGroupId, String serverName, int enabled, int page, int length);

    TableVO<List<AnsibleTaskServerDO>> getLogcleanupTaskLogPage(long id, int page, int length);

    BusinessWrapper<Boolean> updateLogcleanupServers();

    BusinessWrapper<Boolean> updateLogcleanupConfig(long serverId);

    BusinessWrapper<Boolean> save(LogcleanupDO logcleanupDO);

    /**
     * 执行定时清理任务
     */
    void task();

    /**
     *
     * @param id
     * @return
     */
    AnsibleTaskServerDO doTask(long id);

}
