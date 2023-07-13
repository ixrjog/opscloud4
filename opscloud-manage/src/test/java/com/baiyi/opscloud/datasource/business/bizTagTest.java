package com.baiyi.opscloud.datasource.business;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.tag.BusinessTagParam;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import static com.baiyi.opscloud.domain.constants.DsAssetTypeConstants.KUBERNETES_DEPLOYMENT;

/**
 * @Author baiyi
 * @Date 2023/7/12 17:46
 * @Version 1.0
 */
public class bizTagTest extends BaseUnit {


    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private SimpleTagFacade tagFacade;

    @Test
    void test() {
        DsAssetParam.AssetPageQuery query = DsAssetParam.AssetPageQuery.builder()
                .assetType(KUBERNETES_DEPLOYMENT.name())
                .extend(false)
                // ack-frankfurt-prod  46 e0d01a734e484c4cb3f720a317c15b88
                // ack-frankfurt-pre   42 de94322b665d4e24b4e7b095ed84e408
                // ack-frankfurt-sit   87 0887c161a67841628478eae231e302a2
                // ack-frankfurt-daily 38 81bfb61730e14b069541331676ccfc3e
                // ack-frankfurt-dev   37 ed919d9364fb47089a297f5ccf670c2c

                // ack-dev   6   2a81a1ee392e4abd947490513d67da8a
                // ack-daily 10  1a15cc6c2a764fa2bf4fd320496315d2
                // ack-gray  13  40b81f42437a4ed6b4b1e8568ecd1add
                // ack-prod  14  7cdf00acd0824f2083f9ffe624754064

                // eks-prod 30 767faeaff68c4fdb9f9a847ed7ed0689
                // eks-pre  39 3592e35e387f45adac441878cebdf219
                // eks-gray 26 7d125301dabc4bf794a564a0aa63d2ae
                // eks-test 24 06abdd5314a346b79e60322e65ed4135
                .instanceId(30)
                .instanceUuid("767faeaff68c4fdb9f9a847ed7ed0689")
                .page(1)
                .length(100)
                .relation(false)
                .build();

        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(query);
        BusinessTagParam.UpdateBusinessTags updateBusinessTags = BusinessTagParam.UpdateBusinessTags.builder()
                .businessId(0)
                .businessType(BusinessTypeEnum.ASSET.getType())
                 // 25 @London
                 // 52 @Frankfurt
                 // 28 @Hongkong
                 // 24 @Ireland
                .tagIds(Sets.newHashSet(24))
                .build();
        for (DatasourceInstanceAsset asset : table.getData()) {
            updateBusinessTags.setBusinessId(asset.getId());
            tagFacade.updateBusinessTags(updateBusinessTags);
        }
    }

}
