package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 3:59 下午
 * @Version 1.0
 */
public class ZabbixUserGroup {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryUserGroupResponse extends ZabbixResponse.Response {
        private List<UserGroup> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CreateUserGroupResponse extends ZabbixResponse.Response {
        private Usrgrpids result;
    }

    @Data
    public static class Usrgrpids {
        private List<String> usrgrpids;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserGroup implements IToAsset, Serializable {
        @Serial
        private static final long serialVersionUID = 1637937748007727428L;
        @JsonProperty("usrgrpid")
        private String usrgrpid;

        private String name;

        /**
         * 组中用户的前端身份验证方法。
         * 0 - (default) 使用系统默认身份验证方法;
         * 1 - 使用内部认证;
         * 2 - 禁止访问前端。
         */
        @JsonProperty("gui_access")
        private String guiAccess;

        /**
         * 用户组是启用还是禁用。
         * 0 - (default) 启用;
         * 1 - 禁用。
         */
        @JsonProperty("users_status")
        private String status;

        private List<ZabbixUser.User> users;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.usrgrpid)
                    .name(this.name)
                    .assetKey(this.usrgrpid)
                    .assetType(DsAssetTypeConstants.ZABBIX_USER_GROUP.name())
                    .isActive("0".equals(this.status))
                    .kind("zabbixUserGroup")
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}