package com.baiyi.opscloud.service.it;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetDispose;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/30 4:18 下午
 * @Since 1.0
 */
public interface OcItAssetDisposeService {

    void addOcItAssetDispose(OcItAssetDispose ocItAssetApply);

    void delOcItAssetDispose(Integer id);

    OcItAssetDispose queryOcItAssetDisposeByAssetId(Integer assetId);

    DataTable<OcItAssetDispose> queryOcItAssetDisposePage(ItAssetParam.DisposePageQuery pageQuery);
}
