package com.baiyi.opscloud.cloud.vpc;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:14 上午
 * @Version 1.0
 */
public interface ICloudVPC {

    String getKey();

    /**
     * 同步
     *
     * @return
     */
    Boolean syncVPC();
}
