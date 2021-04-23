package com.baiyi.opscloud.jumpserver.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermission;
import com.baiyi.opscloud.jumpserver.bo.PermsAssetpermissionBO;

/**
 * @Author baiyi
 * @Date 2020/3/9 11:51 上午
 * @Version 1.0
 */
public class PermsAssetpermissionBuilder {

    public static PermsAssetpermission build(String name) {
        PermsAssetpermissionBO permsAssetpermissionBO =  PermsAssetpermissionBO.builder()
                .id(UUIDUtils.getUUID())
                .name(name)
               // .dateExpired(TimeUtils.gmtToDate(JumpserverCenterImpl.DATE_EXPIRED))
                .build();
        return covert(permsAssetpermissionBO);
    }

    private static PermsAssetpermission covert(PermsAssetpermissionBO permsAssetpermissionBO) {
        return BeanCopierUtils.copyProperties(permsAssetpermissionBO, PermsAssetpermission.class);
    }
}
