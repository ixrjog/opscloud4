package com.baiyi.opscloud.domain.vo.jumpserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/13 3:23 下午
 * @Version 1.0
 */
public class JumpserverAssetsSystemuserVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetsSystemuser{

        @ApiModelProperty(value = "主键",example="格式为uuid")
        private String id;

        @ApiModelProperty(value = "显示名称")
        private String name;

        @ApiModelProperty(value = "用户名")
        private String username;

    }
}
