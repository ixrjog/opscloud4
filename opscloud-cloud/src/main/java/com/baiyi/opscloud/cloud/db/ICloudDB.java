package com.baiyi.opscloud.cloud.db;

/**
 * @Author baiyi
 * @Date 2020/2/28 7:05 下午
 * @Version 1.0
 */
public interface ICloudDB {

    /**
     * 同步云数据库实例
     * @return
     */
    Boolean syncDBInstance();

    String getKey();

    /**
     * 同步数据库信息
     * @param cloudDbId
     * @return
     */
    Boolean syncDatabase(int cloudDbId);



}
