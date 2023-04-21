package com.baiyi.opscloud.datasource.dingtalk.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/29 4:20 下午
 * @Version 1.0
 */
public class DingtalkUser {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse extends DingtalkResponse.Query implements Serializable {
        @Serial
        private static final long serialVersionUID = -917033148762911968L;
        private Result result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Result extends DingtalkResponse.Result implements Serializable {
        @Serial
        private static final long serialVersionUID = -2508135692366815261L;
        private List<User> list;
    }

    @Data
    public static class User implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = 161013059944723592L;

        private String username; // 转换类处理

        @JsonProperty("dept_order")
        private Long deptOrder;
        private Boolean leader;
        private String extension;
        private String unionid;
        private Boolean boss;
        @JsonProperty("exclusive_account")
        private Boolean exclusiveAccount;
        private String mobile;
        private Boolean active;
        private Boolean admin;
        private String telephone;
        private String remark;
        private String avatar;
        @JsonProperty("hide_mobile")
        private Boolean hideMobile;
        private String title;
        @JsonProperty("hired_date")
        private Date hiredDate;
        private String userid;
        @JsonProperty("org_email")
        private String orgEmail;
        private String name;
        @JsonProperty("dept_id_list")
        private List<Integer> deptIdList;
        @JsonProperty("job_number")
        private String jobNumber;
        @JsonProperty("state_code")
        private String stateCode;
        private String email;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.userid)
                    .name(this.name)
                    .assetKey(this.unionid)
                    .assetKey2(this.email)
                    .assetType(DsAssetTypeConstants.DINGTALK_USER.name())
                    .description(this.title)
                    .isActive(this.active)
                    .kind("user")
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("username", this.username)
                    .paramProperty("mobile", this.mobile)
                    .paramProperty("leader", this.leader)
                    .paramProperty("avatar", this.avatar)
                    .paramProperty("boss", this.boss)
                    .paramProperty("admin", this.admin)
                    .paramProperty("jobNumber", this.jobNumber)
                    .build();
        }

    }

}