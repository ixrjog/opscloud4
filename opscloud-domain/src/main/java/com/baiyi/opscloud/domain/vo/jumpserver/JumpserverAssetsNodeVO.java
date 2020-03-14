package com.baiyi.opscloud.domain.vo.jumpserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/13 12:54 下午
 * @Version 1.0
 */
public class JumpserverAssetsNodeVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetsNode{

        @ApiModelProperty(value = "主键",example="格式为uuid")
        private String id;

        private String value;

    }
}
