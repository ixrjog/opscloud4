package com.baiyi.opscloud.opscloud.vo;

import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/23 5:53 下午
 * @Version 1.0
 */
public class OcUserVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class User {

        private Map<String, UserCredential> credentialMap;

        private List<UserGroupVO.UserGroup> userGroups;

        private List<ServerGroupVO.ServerGroup> serverGroups;

        private Map<String, Object> attributeMap;

        private UserPermissionVO.UserPermission userPermission;

        @ApiModelProperty(value = "主键")
        private Integer id;

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

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserCredential {
        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "凭据类型")
        private Integer credentialType;

        @ApiModelProperty(value = "凭据内容")
        private String credential;

        @ApiModelProperty(value = "凭据指纹")
        private String fingerprint;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
