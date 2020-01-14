package com.baiyi.opscloud.cloudserver;

import com.baiyi.opscloud.domain.generator.OcCloudserver;

/**
 * @Author baiyi
 * @Date 2020/1/10 4:21 下午
 * @Version 1.0
 */
public interface ICloudserver {

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
     * 更新Cloudserver by instanceId
     *
     * @param instanceId
     * @return
     */
    Boolean update(String regionId, String instanceId);

    Boolean powerOn(OcCloudserver ocCloudserver);

    Boolean powerOff(OcCloudserver ocCloudserver);


}
