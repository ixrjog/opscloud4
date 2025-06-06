package com.baiyi.opscloud.domain.vo.leo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/25 11:43
 * &#064;Version 1.0
 */
public class LeoBuildImageVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BuildImage implements Serializable {
        @Serial
        private static final long serialVersionUID = 979354628476222678L;

        private String image;
        private String versionName;
        private String versionDesc;
    }

}
