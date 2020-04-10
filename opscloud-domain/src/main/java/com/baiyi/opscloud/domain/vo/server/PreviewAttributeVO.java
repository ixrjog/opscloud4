package com.baiyi.opscloud.domain.vo.server;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/7 10:30 上午
 * @Version 1.0
 */
public class PreviewAttributeVO {

    @Data
    @ApiModel
    @Builder
    public static class PreviewAttribute {

        private String title;
        private String content;
        @Builder.Default
        private int envType = 0;
        private String path;
    }
}
