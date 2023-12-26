package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:00 下午
 * @Version 1.0
 */
public class DsAssetSubscriptionParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class AssetSubscription {

        private Integer id;

        @NotNull(message = "必须指定数据源实例UUID")
        private String instanceUuid;

        @NotNull(message = "必须指定资产ID")
        private Integer datasourceInstanceAssetId;

        /**
         * 有效
         */
        private Boolean isActive;

        /**
         * 最后订阅时间
         */
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastSubscriptionTime;

        /**
         * 订阅剧本
         */
        private String playbook;

        /**
         * 外部变量
         */
        private String vars;

        /**
         * 最后订阅日志
         */
        private String lastSubscriptionLog;

        private String comment;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AssetSubscriptionPageQuery extends PageParam implements IExtend {

        @Schema(description = "展开")
        private Boolean extend;

        @Schema(description = "是否有效")
        @Builder.Default
        private Boolean isActive = true;

    }

}