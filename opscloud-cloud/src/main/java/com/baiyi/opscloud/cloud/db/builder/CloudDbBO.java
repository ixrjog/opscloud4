package com.baiyi.opscloud.cloud.db.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/28 7:31 下午
 * @Version 1.0
 */
@Builder
@Data
public class CloudDbBO {

    private Integer id;
    private String uid;
    private String accountName;
    private Integer cloudDbType;
    private String regionId;
    private String dbInstanceId;
    private String dbInstanceDescription;
    private String dbInstanceType;
    private String engine;
    private String engineVersion;
    private String zone;
    private String payType;
    private String dbInstanceStatus;
    private Date expiredTime;
    private String instanceNetworkType;
    private String connectionMode;
    private String dbInstanceNetType;
    private String dbInstanceStorageType;
    private String dbInstanceClass;
    private Date createdTime;
    private String category;
    private Date createTime;
    private Date updateTime;
    private String comment;

}
