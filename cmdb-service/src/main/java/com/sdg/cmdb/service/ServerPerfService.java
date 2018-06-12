package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerPerfVO;
import com.sdg.cmdb.domain.server.ServerStatisticsDO;
import com.sdg.cmdb.domain.server.TaskVO;

import java.util.List;

/**
 * Created by liangjian on 2017/2/25.
 */
public interface ServerPerfService {


    /**
     * 获取服务器性能分页数据
     *
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param page
     * @param length
     * @return
     */

    TableVO<List<ServerPerfVO>> getServerPerfPage(long serverGroupId, String serverName, int useType, int envType, String queryIp, int page, int length);

    /**
     * 统计
     * @return
     */
    ServerStatisticsDO statistics();


    TaskVO taskGet();

    BusinessWrapper<Boolean> taskReset();

    BusinessWrapper<Boolean> taskRun();

    /**
     * 从缓存中获取服务器性能监控数据
     * @param serverDO
     * @return
     */
    ServerPerfVO getCache(ServerDO serverDO);

    void cache();

    /**
     * 缓存服务器性能监控
     * @param serverDO
     * @return
     */
    ServerPerfVO cache(ServerDO serverDO);

    /**
     * 计算磁盘空间使用率
     * @param serverDO
     * @return
     */
    float acqDiskRate(ServerDO serverDO);

}
