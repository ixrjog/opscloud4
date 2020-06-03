package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/3 10:23 上午
 * @Version 1.0
 */
public class CloudDBAccountVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudDBAccount {

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "云数据库id")
        private Integer cloudDbId;

        @ApiModelProperty(value = "云数据库实例id")
        private String dbInstanceId;

        @ApiModelProperty(value = "账户名称")
        private String accountName;

        @ApiModelProperty(value = "账号权限")
        private String accountPrivilege;

        @ApiModelProperty(value = "允许工作流申请")
        private Integer workflow;

        @ApiModelProperty(value = "create_time")
        private Date createTime;

        @ApiModelProperty(value = "update_time")
        private Date updateTime;

        @ApiModelProperty(value = "备注")
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PrivilegeAccount {
        @ApiModelProperty(value = "云数据库id")
        private Integer cloudDbId;

        @ApiModelProperty(value = "权限列表")
        private List<String> privileges;

    }
}
