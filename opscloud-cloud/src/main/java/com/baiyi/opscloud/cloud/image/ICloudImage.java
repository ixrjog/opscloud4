package com.baiyi.opscloud.cloud.image;

/**
 * @Author baiyi
 * @Date 2020/3/17 6:35 下午
 * @Version 1.0
 */
public interface ICloudImage {

    String getKey();

    /**
     * 同步
     *
     * @return
     */
    Boolean sync();

}
