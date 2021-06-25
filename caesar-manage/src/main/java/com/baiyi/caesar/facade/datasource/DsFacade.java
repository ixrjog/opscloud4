package com.baiyi.caesar.facade.datasource;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.datasource.DsConfigParam;
import com.baiyi.caesar.domain.param.datasource.DsInstanceParam;
import com.baiyi.caesar.domain.vo.datasource.DsConfigVO;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:34 下午
 * @Version 1.0
 */
public interface DsFacade {

    void setDsInstanceConfig(int instanceId);

    DataTable<DsConfigVO.DsConfig> queryDsConfigPage(DsConfigParam.DsConfigPageQuery pageQuery);

    void addDsConfig(DsConfigVO.DsConfig dsConfig);

    void updateDsConfig(DsConfigVO.DsConfig dsConfig);

    List<DsInstanceVO.Instance> queryDsInstance(DsInstanceParam.DsInstanceQuery query);

    void registerDsInstance(DsInstanceParam.RegisterDsInstance registerDsInstance);
}
