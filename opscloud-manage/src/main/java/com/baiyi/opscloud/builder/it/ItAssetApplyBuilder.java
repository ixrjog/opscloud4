package com.baiyi.opscloud.builder.it;

import com.baiyi.opscloud.common.base.ItAssetStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetApply;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;

import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 4:31 下午
 * @Since 1.0
 */
public class ItAssetApplyBuilder {

    public static OcItAssetApply build(ItAssetParam.ApplyAsset applyAsset) {
        OcItAssetApply ocItAssetApply = new OcItAssetApply();
        ocItAssetApply.setAssetId(applyAsset.getAssetId());
        ocItAssetApply.setUserId(applyAsset.getUserId());
        ocItAssetApply.setUserOrgDeptId(applyAsset.getUserOrgDeptId());
        ocItAssetApply.setApplyType(applyAsset.getApplyType());
        ocItAssetApply.setApplyTime(new Date(applyAsset.getApplyTime()));
        if (applyAsset.getApplyType().equals(ItAssetStatus.BORROW.getType()))
            ocItAssetApply.setExpectReturnTime(new Date(applyAsset.getExpectReturnTime()));
        ocItAssetApply.setIsReturn(false);
        return ocItAssetApply;
    }
}
