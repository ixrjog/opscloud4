package com.baiyi.opscloud.business;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.service.business.BusinessRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.server.ServerService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 5:44 下午
 * @Since 1.0
 */
public class BusinessTest extends BaseUnit {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private BusinessRelationService businessRelationService;

    @Resource
    private ServerService serverService;

    @Test
    void syncServerToEcsRelation() {
        List<DatasourceInstanceAsset> assetList = dsInstanceAssetService.listByInstanceAssetType("86efa9a31e2c431e8fc1c0452ec12133", DsAssetTypeEnum.ECS.getType());
        assetList.forEach(asset -> {
            Server server = serverService.getByPrivateIp(asset.getAssetKey());
            if (server != null) {
                BusinessRelation relation = BusinessRelation.builder()
                        .sourceBusinessType(BusinessTypeEnum.SERVER.getType())
                        .sourceBusinessId(server.getId())
                        .targetBusinessType(BusinessTypeEnum.ASSET.getType())
                        .targetBusinessId(asset.getId())
                        .relationType(asset.getAssetType())
                        .build();
                if (businessRelationService.getByUniqueKey(relation) == null)
                    businessRelationService.add(relation);
            } else {
                System.err.println(JSON.toJSONString(asset));
            }
        });
    }

    @Test
    void test2() {
        System.err.println(JSON.toJSONString(BusinessTypeEnum.SERVER));
    }

}
