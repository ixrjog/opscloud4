package com.baiyi.opscloud.cloud.ram.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/10 11:39 上午
 * @Version 1.0
 */
@Builder
@Data
public class AliyunRamPolicyBO {

    private Integer id;
    private String accountUid;
    private String policyName;
    private String policyType;
    private String description;
    private String defaultVersion;
    @Builder.Default
    private Integer attachmentCount = 0;
    @Builder.Default
    private Integer inWorkorder = 1;
    private String comment;
    @Builder.Default
    private Date createDate = new Date();
    @Builder.Default
    private Date updateDate = new Date();

}
