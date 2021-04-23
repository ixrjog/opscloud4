package com.baiyi.opscloud.domain.vo.server;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/4 2:26 下午
 * @Version 1.0
 */
public class ServerGroupPropertyVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupProperty {

        private Integer id;

        @ApiModelProperty(value = "服务器组id")
        @NotNull(message = "服务器组id不能为空")
        private Integer serverGroupId;

        @ApiModelProperty(value = "环境类型")
        @NotNull(message = "环境类型不能为空")
        private Integer envType;
        private Map<String, String> property;

    }

}
