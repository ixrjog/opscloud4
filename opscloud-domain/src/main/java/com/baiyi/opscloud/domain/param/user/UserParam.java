package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:43 上午
 * @Version 1.0
 */
public class UserParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserPageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "0")
        private Integer extend;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "是否激活")
        private Boolean isActive;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CreateUser {

        @ApiModelProperty(value = "是否研发岗位")
        private Boolean isRD;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @NotBlank(message = "用户名不能为空")
        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "前端框架专用")
        private String uuid;

        @ApiModelProperty(value = "密码")
        private String password;

        @NotBlank(message = "显示名不能为空")
        @ApiModelProperty(value = "显示名")
        private String displayName;

        @ApiModelProperty(value = "姓名")
        private String name;

        @Email
        @ApiModelProperty(value = "邮箱")
        private String email;

        @ApiModelProperty(value = "活跃用户")
        private Boolean isActive;

        @ApiModelProperty(value = "最后登录时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastLogin;

        @ApiModelProperty(value = "微信")
        private String wechat;

        @ApiModelProperty(value = "手机")
        private String phone;

        @ApiModelProperty(value = "创建者")
        private String createdBy;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "留言")
        private String comment;

        @ApiModelProperty(value = "需要绑定钉钉用户id")
        private Integer dingtalkUserId;

        @ApiModelProperty(value = "需要加入的部门id列表")
        private List<Integer> deptIdList;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UpdateUser {

        @ApiModelProperty(value = "主键")
        private Integer id;

        @NotBlank(message = "用户名不能为空")
        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "前端框架专用")
        private String uuid;

        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "显示名")
        private String displayName;

        @ApiModelProperty(value = "姓名")
        private String name;

        @ApiModelProperty(value = "邮箱")
        private String email;

        @ApiModelProperty(value = "活跃用户")
        private Boolean isActive;

        @ApiModelProperty(value = "微信")
        private String wechat;

        @ApiModelProperty(value = "手机")
        private String phone;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "留言")
        private String comment;

        @ApiModelProperty(value = "需要加入的部门id列表")
        private List<Integer> deptIdList;

    }

    @Data
    @Builder
    public static class CreateUserDingTalkMsg {
        private String displayName;
        private String username;
        private String password;
        private String email;
    }


}
