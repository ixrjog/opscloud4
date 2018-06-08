package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.domain.server.serverStatus.ServerCreateByMonthVO;
import com.sdg.cmdb.domain.server.serverStatus.ServerEnvTypeVO;
import com.sdg.cmdb.domain.server.serverStatus.ServerTypeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/9/6.
 */
@Component
public interface ServerDao {


    /**
     * 获取服务器数目
     *
     * @param serverList
     * @param serverName
     * @param useType
     * @param envType
     * @return
     */
    long getServerSize(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp);


    /**
     * 获取Zabbix服务器数目
     *
     * @param serverList
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param zabbixStatus
     * @param zabbixMonitor
     * @return
     */
    long getZabbixServerSize(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp,
            @Param("zabbixStatus") int zabbixStatus,
            @Param("zabbixMonitor") int zabbixMonitor,
            @Param("extTomcatVersion") String extTomcatVersion
    );

    /**
     * 获取服务器详情
     *
     * @param serverList
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param zabbixStatus
     * @param zabbixMonitor
     * @param pageStart
     * @param length
     * @return
     */
    List<ServerDO> getZabbixServerPage(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp,
            @Param("zabbixStatus") int zabbixStatus,
            @Param("zabbixMonitor") int zabbixMonitor,
            @Param("extTomcatVersion") String extTomcatVersion,
            @Param("pageStart") long pageStart, @Param("length") int length);

    /**
     * 只更新server的extTomcatVersion
     * @param serverDO
     * @return
     */
    int updateServerTomcatVersion(ServerDO serverDO);

    /**
     * 获取ECS模版数目
     *
     * @param zoneId
     * @return
     */
    long getEcsTemplateSize(@Param("zoneId") String zoneId);

    /**
     * 获取ECS模版详情
     *
     * @param zoneId
     * @param pageStart
     * @param length
     * @return
     */
    List<EcsTemplateDO> getEcsTemplatePage(
            @Param("zoneId") String zoneId,
            @Param("pageStart") long pageStart, @Param("length") int length);

    EcsTemplateDO queryEcsTemplateById(
            @Param("id") long id);

    /**
     * 获取ECS服务器数目
     *
     * @param serverName
     * @param queryIp
     * @param status
     * @return
     */
    long getEcsServerSize(
            @Param("serverName") String serverName,
            @Param("queryIp") String queryIp,
            @Param("status") int status);


    /**
     * 按id查询vmServer
     *
     * @param id
     * @return
     */
    VmServerDO getVmServerById(@Param("id") long id);

    /**
     * 获取服务器详情
     *
     * @param serverList
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param pageStart
     * @param length
     * @return
     */
    List<ServerDO> getServerPage(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp,
            @Param("pageStart") long pageStart, @Param("length") int length);



    /**
     * 获取ECS服务器详情
     *
     * @param serverName
     * @param queryIp
     * @param status
     * @param pageStart
     * @param length
     * @return
     */
    List<EcsServerDO> getEcsServerPage(
            @Param("serverName") String serverName,
            @Param("queryIp") String queryIp,
            @Param("status") int status,
            @Param("pageStart") long pageStart, @Param("length") int length);


    /**
     * 按finance（是否金融云）查询ECS服务器
     * @param finance
     * @return
     */
    List<EcsServerDO> getEcsServerByFinance(@Param("finance") boolean finance);


    /**
     * 获取vm服务器详情
     *
     * @param serverName
     * @param pageStart
     * @param length
     * @return
     * @parma queryIp
     */
    List<VmServerDO> getVmServerPage(
            @Param("serverName") String serverName,
            @Param("queryIp") String queryIp,
            @Param("status") int status,
            @Param("pageStart") long pageStart, @Param("length") int length);

    /**
     * 获取Vm服务器数目
     *
     * @param serverName
     * @param queryIp
     * @param status
     * @return
     */
    long getVmServerSize(
            @Param("serverName") String serverName,
            @Param("queryIp") String queryIp,
            @Param("status") int status);


    /**
     * 新增指定server group的server
     *
     * @param serverDO
     * @return
     */
    int addServerGroupServer(ServerDO serverDO);

    /**
     * 更新指定servergroup的server信息
     *
     * @param serverDO
     * @return
     */
    int updateServerGroupServer(ServerDO serverDO);

    /**
     * 删除指定servergroup 的 server信息
     *
     * @param id
     * @return
     */
    int delServerGroupServer(@Param("id") long id);

    /**
     * 获取指定的serverid信息
     *
     * @param id
     * @return
     */
    ServerDO getServerInfoById(@Param("id") long id);

    /**
     * 获取指定服务器组下的服务器数目
     *
     * @param groupId
     * @return
     */
    long getServersByGroupId(@Param("groupId") long groupId);


    List<ServerDO> acqServersByGroupId(@Param("groupId") long groupId);


    /**
     * 新增指定ecsServer
     *
     * @param ecsServerDO
     * @return
     */
    int addEcsServer(EcsServerDO ecsServerDO);

    /**
     * 按instanceId查询ecs
     *
     * @param instanceId
     * @return
     */
    EcsServerDO queryEcsByInstanceId(@Param("instanceId") String instanceId);

    int updateEcsServer(EcsServerDO ecsServerDO);

    ServerDO queryServerByInsideIp(@Param("insideIp") String insideIp);

    List<ServerDO> queryServerByServerType(@Param("serverType") int serverType);

    /**
     * 新增vmServer
     *
     * @param vmServerDO
     * @return
     */
    int addVmServer(VmServerDO vmServerDO);

    int updateVmServer(VmServerDO vmServerDO);

    VmServerDO queryVmServerByInsideIp(@Param("insideIp") String insideIp);

    EcsServerDO queryEcsByInsideIp(@Param("insideIp") String insideIp);

    /**
     * 删除server信息
     *
     * @param id
     * @return
     */
    int delServerById(@Param("id") long id);

    /**
     * 删除ecsServer信息
     *
     * @param id
     * @return
     */
    int delEcsServerById(@Param("id") long id);

    /**
     * 删除vmServer信息
     *
     * @param id
     * @return
     */
    int delVmServerById(@Param("id") long id);

    /**
     * ECS服务器统计
     *
     * @return
     */
    ServerStatisticsDO queryEcsStatistics();

    /**
     * VM服务器统计
     *
     * @return
     */
    ServerStatisticsDO queryVmStatistics();

    /**
     * 物理服务器统计
     *
     * @return
     */
    ServerStatisticsDO queryPsStatistics();

    /**
     * 物理服务器（大数据）统计
     *
     * @return
     */
    ServerStatisticsDO queryPsBiStatistics();

    /**
     * 物理服务器（虚拟化）统计
     *
     * @return
     */
    ServerStatisticsDO queryPsVmStatistics();

    /**
     * 获取物理服务器详情
     *
     * @param serverName
     * @param useType
     * @param pageStart
     * @param length
     * @return
     */
    List<PhysicalServerDO> getPhysicalServerPage(
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("pageStart") long pageStart, @Param("length") int length);

    /**
     * 获取物理服务器中的Esxi服务器
     *
     * @return
     */
    List<PhysicalServerDO> getPhysicalServerEsxi();

    /**
     * 获取Vm服务器数目
     *
     * @param serverName
     * @param useType
     * @return
     */
    long getPhysicalServerSize(
            @Param("serverName") String serverName,
            @Param("useType") int useType
    );

    /**
     * 获取服务器成本详情
     *
     * @return
     */
    List<EcsServerDO> getEcsServerCost(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    /**
     * 获取服务器成本详情
     *
     * @return
     */
    List<VmServerDO> getVmServerCost(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);


    /**
     * 查询所有server
     *
     * @return
     */
    List<ServerDO> getAllServer();


    /**
     * 按类型查询所有server
     * @return
     */
    List<ServerDO> getServerByType( @Param("serverType") int serverType );


    /**
     * 获取Zabbix服务器数目
     *
     * @param serverList
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @return
     */
    long getServerPerfSize(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp
    );

    /**
     * 获取服务器详情
     *
     * @param serverList
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param pageStart
     * @param length
     * @return
     */
    List<ServerDO> getServerPerfPage(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp,
            @Param("pageStart") long pageStart, @Param("length") int length);


    /**
     * 服务器性能统计
     *
     * @param serverList
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @return
     */
    List<ServerDO> queryServerPerfStatistics(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp);

    /**
     * 按groupId&envType获取服务器列表
     *
     * @param groupId
     * @param envType
     * @return
     */
    List<ServerDO> getServersByGroupIdAndEnvType(
            @Param("serverGroupId") long groupId,
            @Param("envType") int envType);


    /**
     * 获取服务器数目
     *
     * @param serverGroupId
     * @param envType
     * @return
     */
    long getServerSizeByServerGroupIdAndEnvType(
            @Param("serverGroupId") long serverGroupId,
            @Param("envType") int envType
    );

    /**
     * 更新指定serverUpstream信息
     *
     * @param serverUpstreamDO
     * @return
     */
    int updateServerUpstream(ServerUpstreamDO serverUpstreamDO);

    /**
     * 新增指定serverUpstream信息
     *
     * @param serverUpstreamDO
     * @return
     */
    int addServerUpstream(ServerUpstreamDO serverUpstreamDO);

    /**
     * que
     *
     * @param insideIp
     * @return
     */
    ServerUpstreamDO queryServerUpstreamByIp(
            @Param("insideIp") String insideIp);


    /**
     * 获取VM模版数目
     *
     * @return
     */
    long getVmTemplateSize();

    /**
     * 获取VM模版详情
     *
     * @param pageStart
     * @param length
     * @return
     */
    List<VmTemplateDO> getVmTemplatePage(
            @Param("pageStart") long pageStart, @Param("length") int length);

    /**
     * 查询Ecs属性
     *
     * @param instanceId
     * @param propertyType
     * @param propertyValue
     * @return
     */
    EcsPropertyDO queryEcsProperty(
            @Param("instanceId") String instanceId,
            @Param("propertyType") int propertyType,
            @Param("propertyValue") String propertyValue
    );

    /**
     * 查询Ecs属性
     *
     * @param serverId
     * @return
     */
    List<EcsPropertyDO> queryEcsPropertyByServerId(
            @Param("serverId") long serverId
    );

    /**
     * 删除Ecs属性
     *
     * @param id
     * @return
     */
    int delEcsProperty(@Param("id") long id);


    /**
     * 查询Ecs属性
     *
     * @param instanceId
     * @param propertyType
     * @return
     */
    List<EcsPropertyDO> queryEcsPropertyAll(
            @Param("instanceId") String instanceId,
            @Param("propertyType") int propertyType
    );


    /**
     * 新增EcsProperty
     *
     * @param ecsPropertyDO
     * @return
     */
    int addEcsProperty(EcsPropertyDO ecsPropertyDO);

    /**
     * 按envType统计
     *
     * @return
     */
    List<ServerEnvTypeVO> statusServerEnvType();

    /**
     * 按serverType统计
     *
     * @return
     */
    List<ServerTypeVO> statusServerType();

    /**
     * 按月统计服务器新增
     * @return
     */
    List<ServerCreateByMonthVO> statusServerCreateByMonth();


}
