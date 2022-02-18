package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


public class AuthRoleVO {

    public interface IRoles {

        String getUsername();

        void setRoles(List<Role> roles);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Role extends BaseVO {

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "角色名称")
        private String roleName;

        @ApiModelProperty(value = "访问级别", example = "50")
        private Integer accessLevel;

        @ApiModelProperty(value = "角色描述")
        private String comment;

        @ApiModelProperty(value = "是否支持工单", example = "true")
        private Boolean allowOrder;

    }


}
