package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.logCleanup.LogCleanupPropertyVO;

import java.util.List;

/**
 * Created by liangjian on 2017/3/30.
 */
public interface LogCleanupService {


    /**
     * 清理日志
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<String> cleanup(long serverId);


    /**
     * 清理日志任务
     *
     * @return
     */
    BusinessWrapper<Boolean> task();

    /**
     * 同步数据
     *
     * @return
     */
    void syncData();


    TableVO<List<LogCleanupPropertyVO>> getLogCleanupPage(long serverGroupId, String serverName, int enabled, int page, int length);

    /**
     * 设置是否启用任务
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> setEnabled(long serverId);


    /**
     * 设置History
     *
     * @param cnt
     * @return
     */
    BusinessWrapper<Boolean> setHistory(long serverId, int cnt);

    /**
     * 刷新磁盘使用率
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> refreshDiskRate(long serverId);


    /**
     * 修改日志保留天数
     *
     * @param serverId
     * @param history
     * @return
     */
    BusinessWrapper<Boolean> saveHistory(long serverId, int history);

}
