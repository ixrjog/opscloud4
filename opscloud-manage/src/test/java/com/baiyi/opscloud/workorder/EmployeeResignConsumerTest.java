package com.baiyi.opscloud.workorder;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/3/7 11:38 AM
 * @Version 1.0
 */
@Slf4j
public class EmployeeResignConsumerTest extends BaseUnit {

    @Resource
    private BusinessAssetRelationService bizAssetRelationService;

    /**
     * username : baiyitest , userId : 53
     */
    @Test
    void consumerTest() {
        BusinessAssetRelation relation = BusinessAssetRelation.builder()
                .businessType(BusinessTypeEnum.USER.getType())
                .businessId(53)
                .datasourceInstanceAssetId(9999999)
                .assetType(DsAssetTypeConstants.DINGTALK_USER.name())
                .build();
        bizAssetRelationService.add(relation);
        log.info("BusinessAssetRelation: id={}", relation.getId());
        bizAssetRelationService.delete(relation);
    }

    @Test
    void consumerTest2() {
//        BusinessAssetRelation relation = bizAssetRelationService.getById(140);
//        log.info("BusinessAssetRelation: id = {}", relation.getId());
//        bizAssetRelationService.delete(relation);
    }


}
