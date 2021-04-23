package com.baiyi.opscloud.service.it;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetType;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/20 3:21 下午
 * @Since 1.0
 */
public interface OcItAssetTypeService {

    OcItAssetType queryOcItAssetTypeById(Integer id);

    void addOcItAssetType(OcItAssetType ocItAssetType);

    List<OcItAssetType> queryOcItAssetTypeAll();

    OcItAssetType queryOcItAssetTypeByType(String assetType);
}
