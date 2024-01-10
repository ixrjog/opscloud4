package com.baiyi.opscloud.domain.vo.sys;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

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
    @Schema
    public static class Health {
        private String status;
        private boolean isHealth;
    }

    /**
     * 注册的实例
     */
    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RegisteredInstance extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = 3113987702357024878L;

        @Schema(description = "系统信息")
        private SystemVO.Info systemInfo;

        private Integer id;

        @Schema(description = "实例名")
        private String name;

        @Schema(description = "主机名")
        private String hostname;

        @Schema(description = "主机IP")
        private String hostIp;

        @Schema(description = "实例状态")
        private Integer status;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "说明")
        private String comment;

        private String license;

        @Schema(description = "版本信息")
        private String version;

        /**
         * 活动会话
         */
        private Map<String,Integer> activeSessionMap;

    }

}