package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.param.datasource.DsConfigParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DatasourceConfigMapper extends Mapper<DatasourceConfig> {

  List<DatasourceConfig> queryPageByParam(DsConfigParam.DsConfigPageQuery pageQuery);
}