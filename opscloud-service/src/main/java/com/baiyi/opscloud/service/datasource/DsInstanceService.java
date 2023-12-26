package com.baiyi.opscloud.service.datasource;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 6:07 下午
 * @Version 1.0
 */
public interface DsInstanceService {

    DatasourceInstance getById(Integer id);

    DatasourceInstance getByUuid(String uuid);

    DatasourceInstance getByInstanceName(String name);

    List<DatasourceInstance> queryByParam(DsInstanceParam.DsInstanceQuery query);

    List<DatasourceInstance> listByInstanceType(String instanceType);

    DatasourceInstance getByConfigId(Integer configId);

    void add(DatasourceInstance datasourceInstance);

    void update(DatasourceInstance datasourceInstance);

    int countByConfigId(Integer configId);

}