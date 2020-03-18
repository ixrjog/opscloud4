package com.baiyi.opscloud.domain.vo.jumpserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 3:27 下午
 * @Version 1.0
 */
public class JumpserverSettingsVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Settings {

        @ApiModelProperty(value = "所有管理账户")
        private List<JumpserverAssetsAdminuserVO.AssetsAdminuser> assetsAdminusers;

        @ApiModelProperty(value = "所有系统账户")
        private List<JumpserverAssetsSystemuserVO.AssetsSystemuser> assetsSystemusers;

        @ApiModelProperty(value = "全局设置默认的管理账户")
        private String assetsAdminuserId;

        @ApiModelProperty(value = "全局设置默认的低权限系统账户")
        private String assetsSystemuserId;

        @ApiModelProperty(value = "管理员登录系统账户的高权限账户")
        private String assetsAdminSystemuserId;
    }
}
