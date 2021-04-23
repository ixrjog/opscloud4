package com.baiyi.opscloud.service.it;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetApply;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:08 下午
 * @Since 1.0
 */
public interface OcItAssetApplyService {

    OcItAssetApply queryOcItAssetApplyById(Integer id);

    void addOcItAssetApply(OcItAssetApply ocItAssetApply);

    void updateOcItAssetApply(OcItAssetApply ocItAssetApply);

    DataTable<OcItAssetApply> queryOcItAssetApplyPage(ItAssetParam.ApplyPageQuery pageQuery);

    OcItAssetApply queryOcItAssetApplyByAssetId(Integer assetId);

    List<OcItAssetApply> queryMyAsset(Integer userId);

    List<OcItAssetApply> queryOcItAssetApplyAll();
}
