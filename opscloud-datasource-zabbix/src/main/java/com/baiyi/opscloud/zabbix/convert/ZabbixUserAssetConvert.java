package com.baiyi.opscloud.zabbix.convert;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 3:43 下午
 * @Since 1.0
 */
public class ZabbixUserAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixUser entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getUserid())
                .name(entry.getName())
                .assetKey(entry.getAlias())
                .assetKey2(entry.getSurname())
                .kind(String.valueOf(entry.getType()))
                .assetType(DsAssetTypeEnum.ZABBIX_USER.name())
                .build();
        AssetContainerBuilder builder = AssetContainerBuilder.newBuilder()
                .paramAsset(asset);
        List<ZabbixMedia> medias = entry.getMedias();
        if (!CollectionUtils.isEmpty(medias)) {
            for (ZabbixMedia media : medias) {
                if ("1".equals(media.getMediatypeid())) {
                    //  String email = ZabbixMapper.mapperList(media.getSendto(), String.class).get(0);
                    String email = ((List<String>) media.getSendto()).get(0);
                    builder.paramProperty("email", email);
                    continue;
                }
                if ("3".equals(media.getMediatypeid())) {
                    builder.paramProperty("phone", media.getSendto());
                }
            }
        }
        return builder.build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixUserGroup entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getUsrgrpid())
                .name(entry.getName())
                .assetKey(entry.getUsrgrpid())
                .assetType(DsAssetTypeEnum.ZABBIX_USER_GROUP.name())
                .isActive("0".equals(entry.getStatus()))
                .kind("zabbixUserGroup")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
