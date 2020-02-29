package com.baiyi.opscloud.cloud.db;

/**
 * @Author baiyi
 * @Date 2020/2/28 7:05 下午
 * @Version 1.0
 */
public interface ICloudDB {

    Boolean syncDBInstance();

    String getKey();

}
