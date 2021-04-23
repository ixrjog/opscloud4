package com.baiyi.opscloud.domain.vo.opscloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 4:34 下午
 * @Since 1.0
 */
public class HealthVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Health {
        private String status;

        private boolean isHealth;
    }
}
