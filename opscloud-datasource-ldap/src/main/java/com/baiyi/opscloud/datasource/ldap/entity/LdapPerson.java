package com.baiyi.opscloud.datasource.ldap.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/28 5:41 PM
 * @Version 1.0
 */
public class LdapPerson {

    /**
     * @Author baiyi
     * @Date 2019/12/27 1:04 下午
     * @Version 1.0
     */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @ToString
    @Entry(objectClasses = {"inetorgperson"})
    public static class Person implements IToAsset {
        /**
         * 主键
         */
        @Attribute
        private String personId;

        /**
         * 用户名
         */
        @Attribute(name = "cn")
        private String username;

        /**
         * 显示名
         */
        @Attribute(name = "displayName")
        private String displayName;

        /**
         * 电话
         */
        @Attribute(name = "mobile")
        private String mobile;
        /**
         * 邮箱
         */
        @Attribute(name = "mail")
        private String email;

        /**
         * 工号
         */
        @Attribute(name = "jobNo")
        private String jobNo;

        /**
         * 证件类型
         */
        @Attribute(name = "certType")
        private Integer certType;
        /**
         * 证件号码
         */
        @Attribute(name = "certificateNo")
        private String certNo;

        @Attribute(name = "userPassword")
        private String userPassword;

        @Attribute
        protected Date createTime;

        /**
         * 更新时间
         */
        @Attribute
        protected Date updateTime;

        /**
         * 状态
         */
        @Attribute
        protected Integer status;

        @Attribute
        protected Integer disOrder;

        /**
         * 工作单位
         */
        @Attribute
        private String company;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.username) // 资产id = username
                    .name(this.displayName)
                    .assetKey(this.username)
                    .assetKey2(this.email)
                    .assetType(DsAssetTypeConstants.USER.name())
                    .kind("user")
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("mobile", this.mobile)
                    .build();
        }
    }

}