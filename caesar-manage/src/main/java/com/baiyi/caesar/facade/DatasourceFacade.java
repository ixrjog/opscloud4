package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.datasource.DatasourceConfigParam;
import com.baiyi.caesar.domain.param.datasource.DatasourceInstanceParam;
import com.baiyi.caesar.domain.vo.datasource.DatasourceConfigVO;
import com.baiyi.caesar.domain.vo.datasource.DatasourceInstanceVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:34 下午
 * @Version 1.0
 */
public interface DatasourceFacade {

    DataTable<DatasourceConfigVO.DsConfig> queryDsConfigPage(DatasourceConfigParam.DatasourceConfigPageQuery pageQuery);

    void addDsConfig(DatasourceConfigVO.DsConfig dsConfig);

    void updateDsConfig(DatasourceConfigVO.DsConfig dsConfig);

    List<DatasourceInstanceVO.Instance> queryDsInstance(DatasourceInstanceParam.DsInstanceQuery query);

    void registerDsInstance(DatasourceInstanceParam.RegisterDsInstance registerDsInstance);
}
