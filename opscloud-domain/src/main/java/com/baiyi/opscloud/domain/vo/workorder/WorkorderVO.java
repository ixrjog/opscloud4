package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/28 3:09 下午
 * @Version 1.0
 */
public class WorkorderVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Workorder {

        private String readme;

        private Integer id;
        private String name;
        private String workorderKey;
        private Integer readmeId;
        private Integer workorderGroupId;
        private Integer approvalType;
        private Boolean orgApproval;
        private Integer workorderMode;
        private Integer workorderStatus;
        private String comment;
        private Date createTime;
        private Date updateTime;
        private String approvalDetail;

    }
}
