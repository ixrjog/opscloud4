package com.baiyi.opscloud.domain.vo.jumpserver;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/13 9:56 上午
 * @Version 1.0
 */
public class JumpserverUsersUsergroupVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UsersUsergroup{

        @ApiModelProperty(value = "主键",example="格式为uuid")
        private String id;

        @ApiModelProperty(value = "用户组名称")
        private String name;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date dateCreated;

        private String createdBy;

        @ApiModelProperty(value = "描述")
        private String comment;

    }
}
