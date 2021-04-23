package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetDispose;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcItAssetDisposeMapper extends Mapper<OcItAssetDispose> {

    List<OcItAssetDispose> queryOcItAssetDisposePage(ItAssetParam.DisposePageQuery pageQuery);
}