package com.baiyi.opscloud.domain.vo.leo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2022/11/28 16:55
 * @Version 1.0
 */
public class LeoBuildPipelineVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Step implements Serializable {

        @Serial
        private static final long serialVersionUID = 1740884710532256025L;

        private String displayDescription;
        private String displayName;
        private String id;
        private String result;
        private String state;
        private String type;
        private String log;

    }

}