package com.baiyi.opscloud.cloud.db.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/29 7:31 下午
 * @Version 1.0
 */
@Data
@Builder
public class CloudDbAttributeBO {

    private Integer id;
    private Integer cloudDbId;
    private String dbInstanceId;
    private String attributeName;
    private String attributeValue;
    private Date createTime;
    private Date updateTime;
    private String comment;
}
