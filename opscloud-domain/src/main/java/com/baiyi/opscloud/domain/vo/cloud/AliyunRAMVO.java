package com.baiyi.opscloud.domain.vo.cloud;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 5:43 下午
 * @Version 1.0
 */
public class AliyunRAMVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class RAMUser {

        private List<RAMPolicy> policies;
        private String ramAccount;
        private String ramAccountLoginUrl;

        private Integer id;
        private String accountUid;
        private String ramUserId;
        private String ramUsername;
        private String ramDisplayName;
        private String mobile;
        private Integer accessKeys;
        private Integer ramType;
        private String comment;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateDate;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class RAMPolicy {

        @ApiModelProperty(value = "主账户名称")
        private String accountName;

        private Integer id;
        private String accountUid;
        private String policyName;
        private String policyType;
        private String description;
        private String defaultVersion;
        private Integer attachmentCount;
        private Integer inWorkorder;
        private String comment;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateDate;
    }
}
