package com.baiyi.opscloud.domain.vo.datasource;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:09 下午
 * @Version 1.0
 */
public class DsAssetSubscriptionVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetSubscription extends BaseVO implements DsInstanceVO.IDsInstance, DsAssetVO.IDsAsset, ShowTime.IAgo {

        private DsInstanceVO.Instance instance;

        private DsAssetVO.Asset asset;

        public Date getAgoTime() {
            return lastSubscriptionTime;
        }

        private String ago;

        @Override
        public Integer getAssetId() {
            return datasourceInstanceAssetId;
        }

        private Integer id;

        @NotNull(message = "必须指定数据源实例uuid")
        private String instanceUuid;

        @NotNull(message = "必须指定资产id")
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
}
