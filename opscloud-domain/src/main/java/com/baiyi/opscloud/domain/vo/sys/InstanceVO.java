package com.baiyi.opscloud.domain.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:49 下午
 * @Version 1.0
 */
public class InstanceVO {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class Health {
        private String status;

        private boolean isHealth;
    }
}
