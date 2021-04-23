package com.baiyi.opscloud.service.it;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAsset;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 2:40 下午
 * @Since 1.0
 */
public interface OcItAssetService {

    OcItAsset queryOcItAssetById(Integer id);

    void addOcItAsset(OcItAsset ocItAsset);

    void updateOcItAsset(OcItAsset ocItAsset);

    void deleteOcItAssetById(int id);

    DataTable<OcItAsset> queryOcItAssetPage(ItAssetParam.PageQuery pageQuery);

    OcItAsset queryOcItAssetByAssetCode(String assetCode);

    int countOcItAssetByStatus(Integer assetStatus);

    int countOcItAsset();

    List<OcItAsset> queryOcItAssetAll();
}
