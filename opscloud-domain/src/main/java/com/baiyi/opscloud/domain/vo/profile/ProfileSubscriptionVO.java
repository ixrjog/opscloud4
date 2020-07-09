package com.baiyi.opscloud.domain.vo.profile;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:02 上午
 * @Version 1.0
 */
public class ProfileSubscriptionVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ProfileSubscription {

        private Integer id;
        private String subscriptionType;
        private String name;
        private String hostPattern;
        private String comment;
        private Integer serverTaskId;
        private Date executionTime;
        private Date createTime;
        private Date updateTime;
        private String vars;

    }
}
