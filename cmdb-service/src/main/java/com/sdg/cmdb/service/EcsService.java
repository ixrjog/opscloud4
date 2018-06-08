package com.sdg.cmdb.service;


import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.*;

import java.util.List;

/**
 * Created by liangjian on 2016/12/22.
 */
public interface EcsService {


    /**
     * 获取ECS服务器分页数据
     *
     * @param serverName
     * @praam queryIp
     * @param status
     * @param page
     * @param length
     * @return
     */
    TableVO<List<EcsServerVO>> getEcsServerPage(String serverName, String queryIp, int status, int page, int length);

    /**
     * 查询ecs状态
     *
     * @param regionId
     * @return
     */
    EcsServerDO ecsStatus(String regionId, ServerDO serverDO);

    /**
     * 停止实例
     *
     * @param regionId
     * @param forceStop 重启机器时的是否强制关机策略。默认值为 false
     * @return
     */
    boolean ecsStop(String regionId, boolean forceStop, ServerDO serverDO);

    /**
     * 启动实例
     * @param instanceId
     * @return
     */
    boolean powerOn(String instanceId);

    /**
     * 重启实例(服务器正常启动状态才能使用)
     *
     * @param regionId
     * @return
     */
    boolean ecsReboot(String regionId, ServerDO serverDO);

    /**
     * 设置ECS状态
     *
     * @param insideIp
     * @return
     */
    BusinessWrapper<Boolean> setStatus(String insideIp, int status);

    /**
     * 删除ECS & Server
     *
     * @param insideIp
     * @return
     */
    BusinessWrapper<Boolean> delEcs(String insideIp);


    /**
     * 刷新数据（此接口废弃）
     *
     * @return
     */
    BusinessWrapper<Boolean> ecsRefresh();


    /**
     * 同步并更新ECS列表
     * @param type  0 阿里云 1 金融云
     * @return
     */
    BusinessWrapper<Boolean> ecsSync(int type);

    /**
     * 校验数据
     *
     * @return
     */
    BusinessWrapper<Boolean> ecsCheck();


    /**
     * 统计
     * @return
     */
    ServerStatisticsDO statistics();


    /**
     * 获取ECS模版机分页数据
     * @param zoneId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<EcsTemplateDO>> getEcsTemplatePage(String zoneId, int page, int length);

    /**
     * 从server表更新ecsServer数据（content,serverId 字段数据）
     * @param ecsServerDO
     * @return
     */
    boolean updateEcsServerForServer(EcsServerDO ecsServerDO);


    /**
     * 从server表更新ecsServer数据（content,serverId 字段数据）
     * @param serverDO
     * @return
     */
    boolean updateEcsServerForServer(ServerDO serverDO);

    /**
     * 按instanceId查询ecs
     * @param regionId
     * @param instanceId
     * @return
     */
    EcsServerDO ecsGet(String regionId, String instanceId);

    DescribeInstancesResponse.Instance query(String regionId, String instanceId);

    /**
     * 查询磁盘
     * @param regionId
     * @param instanceId
     * @return
     */
    List<DescribeDisksResponse.Disk> queryDisks(String regionId, String instanceId, boolean isFinance);

    /**
     * 设置ecs属性
     * @param ecsServerDO
     * @param propertyType
     * @param value
     * @return
     */
    boolean setEcsProperty(EcsServerDO ecsServerDO, int propertyType, String value);

    /**
     * 查询ecs属性
     * @param ecsServerDO
     * @param propertyType
     * @return
     */
    EcsPropertyDO getEcsProperty(EcsServerDO ecsServerDO, int propertyType);

    /**
     * 查询ecs属性
     * @param ecsServerDO
     * @param propertyType
     * @return
     */
    List<EcsPropertyDO> getEcsPropertyAll(EcsServerDO ecsServerDO, int propertyType);

    /**
     * 保存ecsServer的所有属性
     * @param ecsServerDO
     * @return
     */
    boolean saveEcsServerProperty(EcsServerDO ecsServerDO);

    /**
     * 清理ecs扩展属性
     * @param serverDO
     * @return
     */
    boolean delEcsProperty(ServerDO serverDO);

    void invokeEcsServerVO(EcsServerVO ecsServerVO);
}
