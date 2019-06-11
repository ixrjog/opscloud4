package com.sdg.cmdb.dao.cmdb;


import com.sdg.cmdb.domain.logCleanup.LogcleanupDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/3/30.
 */
@Component
public interface LogcleanupDao {

    /**
     * 获取日志清理配置数目
     *
     * @param serverGroupId
     * @param serverName
     * @param enabled
     * @return
     */
    long getLogcleanupSize(
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("enabled") int enabled
    );

    /**
     * 获取日志清理配置详情
     *
     * @param serverGroupId
     * @param serverName
     * @param enabled
     * @param pageStart
     * @param length
     * @return
     */
    List<LogcleanupDO> getLogcleanupPage(
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("enabled") int enabled,
            @Param("pageStart") long pageStart,
            @Param("length") int length);

    LogcleanupDO getLogcleanup(@Param("id") long id);

    List<LogcleanupDO> getLogcleanupByEnabled();

    LogcleanupDO getLogcleanupByServerId(@Param("serverId") long serverId);

    /**
     * 更新服务器信息
     * @param logcleanupDO
     * @return
     */
    int updateLogcleanupServerInfo(LogcleanupDO logcleanupDO);


    int addLogcleanup(LogcleanupDO logcleanupDO);

    int updateLogcleanup(LogcleanupDO logcleanupDO);

    /**
     * 更新配置相关字段
     * @param logcleanupDO
     * @return
     */
    int updateLogcleanupConfig(LogcleanupDO logcleanupDO);

    /**
     * 更新任务相关字段
     * @param logcleanupDO
     * @return
     */
    int updateLogcleanupTask(LogcleanupDO logcleanupDO);

    int delLogcleanup(@Param("id") long id);

//    /**
//     * 查询所有启用的LogCleanupPropertyDO条目
//     * @return
//     */
//    List<LogCleanupPropertyDO> getAllEnabledLogCleanupProperty();
//
//
//    List<LogCleanupPropertyDO> getAllLogCleanupProperty();
//
//    /**
//     * 按serverGroupId&envType查找
//     *
//     * @param serverId
//     * @return
//     */
//    LogCleanupPropertyDO getLogCleanupPropertyByServerId(
//            @Param("serverId") long serverId );
//
//    /**
//     * 新增LogCleanupProperty
//     * @param logCleanupPropertyDO
//     * @return
//     */
//    int addLogCleanupProperty(LogCleanupPropertyDO logCleanupPropertyDO);
//
//    /**
//     * 更新LogCleanupProperty
//     * @param logCleanupPropertyDO
//     * @return
//     */
//    int updateLogCleanupProperty(LogCleanupPropertyDO logCleanupPropertyDO);
//
//    int updateLogCleanupPropertyByResult(@Param("cleanupResult") boolean cleanupResult);
//
//    /**
//     * 按envType查找配置项
//     *
//     * @param envType
//     * @return
//     */
//    LogCleanupConfigurationDO getLogCleanupConfigurationByEnvType(
//            @Param("envType") int envType);
//
//
//    int delLogCleanupProperty(@Param("id") long id);
}
