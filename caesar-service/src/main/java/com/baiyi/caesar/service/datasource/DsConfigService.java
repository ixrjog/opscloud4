package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.param.datasource.DatasourceConfigParam;

import java.util.List;


/**
 * @Author baiyi
 * @Date 2021/5/15 1:45 下午
 * @Version 1.0
 */
public interface DsConfigService {

    DatasourceConfig getById(Integer id);

    DataTable<DatasourceConfig> queryPageByParam(DatasourceConfigParam.DatasourceConfigPageQuery pageQuery);

    void add(DatasourceConfig datasourceConfig);

    void update(DatasourceConfig datasourceConfig);

    List<DatasourceConfig> queryByDsType(Integer dsType);
}
