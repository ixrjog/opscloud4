package com.baiyi.opscloud.jumpserver.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.jumpserver.bo.AssetsNodeBO;

/**
 * @Author baiyi
 * @Date 2020/3/9 10:55 上午
 * @Version 1.0
 */
public class AssetsNodeBuilder {

    public static AssetsNode build(OcServerGroup ocServerGroup,String key) {
        AssetsNodeBO assetsNodeBO = AssetsNodeBO.builder()
                .id(UUIDUtils.getUUID())
                .value(ocServerGroup.getName())
                .key(key)
                .build();
        return covert(assetsNodeBO);
    }

    private static AssetsNode covert(AssetsNodeBO assetsNodeBO) {
        return BeanCopierUtils.copyProperties(assetsNodeBO, AssetsNode.class);
    }
}
