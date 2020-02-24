package com.baiyi.opscloud.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:54 下午
 * @Version 1.0
 */
public class OcUserGroupVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserGroup {

        private List<OcUserVO.User> users;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "用户组名称")
        private String name;

        @ApiModelProperty(value = "用户组类型")
        private Integer grpType;

        @ApiModelProperty(value = "允许工资流申请")
        private Integer workflow;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "留言")
        private String comment;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
