package com.baiyi.opscloud.datasource.aliyun.domain.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/4/18 13:24
 * @Version 1.0
 */
public class AliyunDomain {

    @Data
    public static class Domain implements IToAsset {

        private String domainName;
        private String instanceId;
        private String expirationDate;
        private String registrationDate;
        private String domainType;
        private String domainStatus;
        private String productId;
        private Long expirationDateLong;
        private Long registrationDateLong;
        private Boolean premium;
        private String domainAuditStatus;
        private String expirationDateStatus;
        private String registrantType;
        private String domainGroupId;
        private String remark;
        private String domainGroupName;
        private Integer expirationCurrDateDiff;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.domainName)
                    .name(this.domainName)
                    .assetKey(this.domainName)
                    .kind("domain")
                    .assetType(DsAssetTypeConstants.ALIYUN_DOMAIN.name())
                    .description(this.remark)
                    .expiredTime(new Date(this.expirationDateLong))
                    .createdTime(new Date(this.registrationDateLong))
                    .build();

            return AssetContainerBuilder.newBuilder().paramAsset(asset)
                    // 域名实名认证状态。取值: FAILED：实名认证失败、SUCCEED：实名认证成功、NONAUDIT：未实名认证、AUDITING：审核中
                    .paramProperty("DomainAuditStatus", this.domainAuditStatus)
                    // 域名类型。取值：New gTLD、gTLD、ccTLD。
                    .paramProperty("DomainType", this.domainType)
                    // 域名到期日和当前的时间的天数差值。
                    .paramProperty("ExpirationCurrDateDiff", this.expirationCurrDateDiff)
                    // 域名过期状态。取值：1：域名未过期。2：域名已过期。
                    .paramProperty("ExpirationDateStatus", this.expirationDateStatus)
                    // 实例编号
                    .paramProperty("InstanceId", this.instanceId).build();
        }

    }

}