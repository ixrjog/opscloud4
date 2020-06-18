package com.baiyi.opscloud.cloud.db.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/1 6:32 下午
 * @Version 1.0
 */
@Builder
@Data
public class CloudDbDatabaseBO {

    private Integer id;
    private Integer cloudDbId;
    private String dbInstanceId;
    private String dbName;
    private String engine;
    private String dbDescription;
    private String dbStatus;
    private String characterSetName;
    @Builder.Default
    private Integer envType = 0;
    private Date createTime;
    private Date updateTime;
    private String comment;
}
