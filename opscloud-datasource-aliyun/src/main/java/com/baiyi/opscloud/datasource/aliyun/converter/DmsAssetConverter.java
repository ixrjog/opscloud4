package com.baiyi.opscloud.datasource.aliyun.converter;

import com.baiyi.opscloud.datasource.aliyun.dms.entity.DmsUser;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

/**
 * @Author baiyi
 * @Date 2021/12/16 4:24 PM
 * @Version 1.0
 */
public class DmsAssetConverter {

    public static DmsUser.User toDmsUser(DatasourceInstanceAsset asset, String mobile) {
        return DmsUser.User.builder()
                .nickName(asset.getName())
                .mobile(mobile)
                .uid(asset.getAssetId())
                .build();
    }

}