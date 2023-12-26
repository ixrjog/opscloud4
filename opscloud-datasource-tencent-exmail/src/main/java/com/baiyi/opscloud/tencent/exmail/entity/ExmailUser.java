package com.baiyi.opscloud.tencent.exmail.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.tencent.exmail.entity.base.BaseExmailResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/10/12 5:40 下午
 * @Since 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExmailUser extends BaseExmailResult implements IToAsset {

    /**
     * 企业邮帐号名，邮箱格式
     */
    @JsonProperty("userid")
    private String userId;
    private String name;

    private List<Long> department;
    /**
     * 职位信息
     */
    private String position;
    private String mobile;
    private String tel;
    /**
     * 别名列表
     * 1、Slaves上限为5个
     * 2、Slaves为邮箱格式
     */
    private List<String> slaves;

    /**
     * 1 true
     * 0 false
     */
    private String enable;

    @Override
    public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(this.userId)
                .name(this.name)
                .assetKey(this.userId)
                // 用户名
                .assetKey2(this.userId.split("@")[0])
                .description(this.position)
                .isActive("1".equals(this.enable))
                .assetType(DsAssetTypeConstants.TENCENT_EXMAIL_USER.name())
                .kind("user")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobile", this.mobile)
                .build();
    }

}