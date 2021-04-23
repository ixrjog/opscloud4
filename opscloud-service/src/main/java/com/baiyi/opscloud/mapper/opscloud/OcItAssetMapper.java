package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAsset;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcItAssetMapper extends Mapper<OcItAsset> {

    List<OcItAsset> queryOcItAssetPage(ItAssetParam.PageQuery pageQuery);

}