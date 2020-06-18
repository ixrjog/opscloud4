package com.baiyi.opscloud.server.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/6 5:52 下午
 * @Version 1.0
 */
@Builder
@Data
public class ServerAttributeBO {

    private Integer id;
    private Integer businessId;
    private Integer businessType;
    private String attributeType;
    private String groupName;
    private Date createTime;
    private Date updateTime;
    private String attributes;
    private String comment;

}
