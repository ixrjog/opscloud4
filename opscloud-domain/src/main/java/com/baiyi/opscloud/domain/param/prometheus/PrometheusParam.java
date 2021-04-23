package com.baiyi.opscloud.domain.param.prometheus;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/5 4:03 下午
 * @Since 1.0
 */
public class PrometheusParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SaveConfig {

        @NotNull
        private String content;

    }
}
