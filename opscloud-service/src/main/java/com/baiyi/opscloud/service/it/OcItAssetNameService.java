package com.baiyi.opscloud.service.it;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetName;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/20 3:22 下午
 * @Since 1.0
 */
public interface OcItAssetNameService {

    OcItAssetName queryOcItAssetNameById(Integer id);

    void addOcItAssetName(OcItAssetName ocItAssetName);

    List<OcItAssetName> queryOcItAssetNameByType(Integer assetTypeId);

    OcItAssetName queryOcItAssetNameByName(String assetName);

    List<OcItAssetName> queryOcItAssetNameAll();

}
