package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;
import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
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
 * @Date 2020/2/20 11:20 上午
 * @Version 1.0
 */
public class UserVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class User {

        private Map<String, UserCredentialVO.UserCredential> credentialMap;

        private List<UserGroupVO.UserGroup> userGroups;

        private List<ServerGroupVO.ServerGroup> serverGroups;

        private List<UserApiTokenVO.UserApiToken> apiTokens; // 只显示有效的ApiToken

        private Map<String, Object> attributeMap;

        private List<AliyunRAMVO.RAMUser> ramUsers;

        private Map<String, List<OcOrgDepartment>> orgDeptMap; // 所在部门

        private List<ItAssetVO.Asset> assets; // 我的资产

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

}
