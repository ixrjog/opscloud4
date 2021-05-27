package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.param.datasource.DatasourceConfigParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DatasourceConfigMapper extends Mapper<DatasourceConfig> {

  List<DatasourceConfig> queryPageByParam(DatasourceConfigParam.DatasourceConfigPageQuery pageQuery);
}