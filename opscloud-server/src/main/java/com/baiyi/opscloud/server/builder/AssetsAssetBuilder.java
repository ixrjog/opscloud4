package com.baiyi.opscloud.server.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.bo.AssetsAssetBO;

/**
 * @Author baiyi
 * @Date 2020/3/9 2:37 下午
 * @Version 1.0
 */
public class AssetsAssetBuilder {

    public static AssetsAsset build(OcServer ocServer, String ip, String adminUserId, String hostname,Integer port, String comment) {

        AssetsAssetBO assetsAssetBO = AssetsAssetBO.builder()
                .id(UUIDUtils.getUUID())
                .ip(ip)
                .publicIp(ocServer.getPublicIp() != null ? ocServer.getPublicIp() : "")
                .adminUserId(adminUserId)
                .hostname(hostname)
                .port(port)
                .comment(comment)
                .build();
//        if (!StringUtils.isEmpty(ocServer.getComment()))
//            assetsAssetBO.setComment(ocServer.getComment());
        return covert(assetsAssetBO);
    }

    private static AssetsAsset covert(AssetsAssetBO assetsAssetBO) {
        return BeanCopierUtils.copyProperties(assetsAssetBO, AssetsAsset.class);
    }
}
