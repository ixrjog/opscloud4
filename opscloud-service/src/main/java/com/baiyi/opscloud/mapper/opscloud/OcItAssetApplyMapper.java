package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetApply;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcItAssetApplyMapper extends Mapper<OcItAssetApply> {

    List<OcItAssetApply> queryOcItAssetApplyPage(ItAssetParam.ApplyPageQuery pageQuery);

}