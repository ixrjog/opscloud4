package com.baiyi.opscloud.service.datasource;

import com.baiyi.opscloud.factory.credential.base.ICredentialCustomer;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.param.datasource.DsConfigParam;

import java.util.List;


/**
 * @Author baiyi
 * @Date 2021/5/15 1:45 下午
 * @Version 1.0
 */
public interface DsConfigService extends ICredentialCustomer {

    DatasourceConfig getById(Integer id);

    DataTable<DatasourceConfig> queryPageByParam(DsConfigParam.DsConfigPageQuery pageQuery);

    void add(DatasourceConfig datasourceConfig);

    void update(DatasourceConfig datasourceConfig);

    List<DatasourceConfig> queryByDsType(Integer dsType);

}