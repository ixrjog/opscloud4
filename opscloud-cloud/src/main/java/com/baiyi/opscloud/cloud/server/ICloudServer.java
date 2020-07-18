package com.baiyi.opscloud.cloud.server;

import com.baiyi.opscloud.domain.BusinessWrapper;

/**
 * @Author baiyi
 * @Date 2020/1/10 4:21 下午
 * @Version 1.0
 */
public interface ICloudServer {

    String getKey();

    /**
     * 同步
     *
     * @return
     */
    Boolean sync();

    /**
     * 同步并推送主机名
     *
     * @param pushName
     * @return
     */
    Boolean sync(boolean pushName);

    /**
     * 录入实例
     *
     * @param regionId
     * @param instanceId
     * @return
     */
    Boolean record(String regionId, String instanceId);

    /**
     * 设置云服务器离线
     *
     * @param serverId
     * @return
     */
    void offline(int serverId);

    /**
     * 更新CloudServer by instanceId
     *
     * @param instanceId
     * @return
     */
    Boolean update(String regionId, String instanceId);

    /**
     * 开机
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> start(Integer id);

    /**
     * 关机
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> stop(Integer id);

    /**
     * 查询电源状态
     *
     * @param id
     * @return
     */
    int queryPowerStatus(Integer id);

    /**
     * 释放云服务器
     *
     * @param instanceId
     * @return
     */
    Boolean delete(String instanceId);
}
