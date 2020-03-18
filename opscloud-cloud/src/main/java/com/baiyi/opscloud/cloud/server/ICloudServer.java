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
     * 更新CloudServer by instanceId
     *
     * @param instanceId
     * @return
     */
    Boolean update(String regionId, String instanceId);

    /**
     * 开机
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> start(Integer id);

    /**
     * 关机
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> stop(Integer id);


}
