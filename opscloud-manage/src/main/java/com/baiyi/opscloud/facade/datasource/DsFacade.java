package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsConfigParam;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.vo.datasource.DsConfigVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:34 下午
 * @Version 1.0
 */
public interface DsFacade {

    void setDsInstanceConfig(int instanceId);

    DataTable<DsConfigVO.DsConfig> queryDsConfigPage(DsConfigParam.DsConfigPageQuery pageQuery);

    void addDsConfig(DsInstanceParam.AddDsConfig dsConfig);

    void updateDsConfig(DsInstanceParam.UpdateDsConfig dsConfig);

    List<DsInstanceVO.Instance> queryDsInstance(DsInstanceParam.DsInstanceQuery query);

    void registerDsInstance(DsInstanceParam.RegisterDsInstance registerDsInstance);

    DsInstanceVO.Instance queryDsInstanceById(int instanceId);

    DsConfigVO.DsConfig queryDsConfigById(int configId);
}
