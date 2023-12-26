package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 2:32 下午
 * @Version 1.0
 */
public class ZabbixUser {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryUserResponse extends ZabbixResponse.Response {
        private List<User> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class UpdateUserResponse extends ZabbixResponse.Response {
        private Userids result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CreateUserResponse extends UpdateUserResponse {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class DeleteUserResponse extends UpdateUserResponse {
    }

    @Data
    public static class Userids {
        private List<String> userids;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements IToAsset, Serializable {
        @Serial
        private static final long serialVersionUID = -8414101569080017905L;
        // @JsonProperty("userid")
        private String userid;
        private String alias;
        private String name;

        /**
         * 姓
         */
        private String surname;

        /**
         * 允许自动登录。
         * 0 - (default) 禁止自动登录;
         * 1 -允许自动登录。
         */
        //@JsonProperty("autologin")
        private Integer autologin;

        /**
         * 会话过期时间。 接受具有后缀的秒或时间单位。 如果设置为 0s, 用户登录会话永远不会过期。
         */
        //@JsonProperty("autologout")
        private String autologout;

        /**
         * 用户类型。
         * 1 - (default) Zabbix user;
         * 2 - Zabbix admin;
         * 3 - Zabbix super admin.
         */
        private Integer type;
        //@JsonProperty("sessionid")
        private String sessionid;

        /**
         * 用户使用的媒体
         */
        private List<ZabbixMedia.Media> medias;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.userid)
                    .name(this.name)
                    .assetKey(this.alias)
                    .assetKey2(this.surname)
                    .kind(String.valueOf(this.type))
                    .assetType(DsAssetTypeConstants.ZABBIX_USER.name())
                    .build();
            AssetContainerBuilder builder = AssetContainerBuilder.newBuilder()
                    .paramAsset(asset);
            if (!CollectionUtils.isEmpty(this.medias)) {
                for (ZabbixMedia.Media media : this.medias) {
                    if ("1".equals(media.getMediatypeid())) {
                        //  String email = ZabbixMapper.mapperList(media.getSendto(), String.class).get(0);
                        try {
                            String email = ((List<String>) media.getSendto()).getFirst();
                            builder.paramProperty("email", email);
                        } catch (Exception ignored) {
                        }
                        continue;
                    }
                    if ("3".equals(media.getMediatypeid())) {
                        builder.paramProperty("phone", media.getSendto());
                    }
                }
            }
            return builder.build();
        }
    }

}