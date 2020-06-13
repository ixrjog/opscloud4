package com.baiyi.opscloud.domain.vo.server;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/4/7 10:30 上午
 * @Version 1.0
 */
public class PreviewAttributeVO {

    @Data
    @ApiModel
    @Builder
    public static class PreviewAttribute implements Serializable {
        private static final long serialVersionUID = -5982665589742345590L;
        @Builder.Default
        private String title = "";
        @Builder.Default
        private String content = "";
        @Builder.Default
        private int envType = 0;
        private String path;
    }
}
