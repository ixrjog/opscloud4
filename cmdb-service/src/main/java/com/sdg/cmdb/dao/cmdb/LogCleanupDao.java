package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.logCleanup.LogCleanupConfigurationDO;
import com.sdg.cmdb.domain.logCleanup.LogCleanupPropertyDO;
import com.sdg.cmdb.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/3/30.
 */
@Component
public interface LogCleanupDao {

    /**
     * 获取日志清理配置数目
     * @param serverGroupId
     * @param serverName
     * @param enabled
     * @return
     */
    long getLogCleanupPropertySize(
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("enabled") int enabled
    );

    /**
     * 获取日志清理配置详情
     * @param serverGroupId
     * @param serverName
     * @param enabled
     * @param pageStart
     * @param length
     * @return
     */
    List<LogCleanupPropertyDO> getLogCleanupPropertyPage(
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("enabled") int enabled,
            @Param("pageStart") long pageStart,
            @Param("length") int length);

    /**
     * 查询所有启用的LogCleanupPropertyDO条目
     * @return
     */
    List<LogCleanupPropertyDO> getAllEnabledLogCleanupProperty();


    List<LogCleanupPropertyDO> getAllLogCleanupProperty();

    /**
     * 按serverGroupId&envType查找
     *
     * @param serverId
     * @return
     */
    LogCleanupPropertyDO getLogCleanupPropertyByServerId(
            @Param("serverId") long serverId );

    /**
     * 新增LogCleanupProperty
     * @param logCleanupPropertyDO
     * @return
     */
    int addLogCleanupProperty(LogCleanupPropertyDO logCleanupPropertyDO);

    /**
     * 更新LogCleanupProperty
     * @param logCleanupPropertyDO
     * @return
     */
    int updateLogCleanupProperty(LogCleanupPropertyDO logCleanupPropertyDO);

    int updateLogCleanupPropertyByResult(@Param("cleanupResult") boolean cleanupResult);

    /**
     * 按envType查找配置项
     *
     * @param envType
     * @return
     */
    LogCleanupConfigurationDO getLogCleanupConfigurationByEnvType(
            @Param("envType") int envType);


    int delLogCleanupProperty(@Param("id") long id);
}
