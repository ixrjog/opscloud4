package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.param.datasource.DatasourceConfigParam;
import com.baiyi.caesar.domain.param.datasource.DatasourceInstanceParam;
import com.baiyi.caesar.facade.DatasourceFacade;
import com.baiyi.caesar.packer.datasource.DatasourceConfigPacker;
import com.baiyi.caesar.packer.datasource.DatasourceInstancePacker;
import com.baiyi.caesar.service.datasource.DsConfigService;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import com.baiyi.caesar.domain.vo.datasource.DatasourceConfigVO;
import com.baiyi.caesar.domain.vo.datasource.DatasourceInstanceVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:35 下午
 * @Version 1.0
 */
@Service
public class DatasourceFacadeImpl implements DatasourceFacade {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsInstanceService dsInstancService;

    @Resource
    private DatasourceConfigPacker dsConfigPacker;

    @Resource
    private DatasourceInstancePacker dsInstancePacker;

    @Override
    public DataTable<DatasourceConfigVO.DsConfig> queryDsConfigPage(DatasourceConfigParam.DatasourceConfigPageQuery pageQuery) {
        DataTable<DatasourceConfig> table = dsConfigService.queryPageByParam(pageQuery);
        return new DataTable<>(dsConfigPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void addDsConfig(DatasourceConfigVO.DsConfig dsConfig) {
        DatasourceConfig datasourceConfig = BeanCopierUtil.copyProperties(dsConfig, DatasourceConfig.class);
        datasourceConfig.setUuid(IdUtil.buildUUID());
        dsConfigService.add(datasourceConfig);
    }

    @Override
    public void updateDsConfig(DatasourceConfigVO.DsConfig dsConfig) {
        dsConfigService.update(BeanCopierUtil.copyProperties(dsConfig, DatasourceConfig.class));
    }

    @Override
    public List<DatasourceInstanceVO.Instance> queryDsInstance(DatasourceInstanceParam.DsInstanceQuery query) {
        List<DatasourceInstance> instanceList = dsInstancService.queryByParam(query);
        return dsInstancePacker.wrapVOList(instanceList, query);
    }

    @Override
    public void registerDsInstance(DatasourceInstanceParam.RegisterDsInstance registerDsInstance) {
        DatasourceInstance datasourceInstance = BeanCopierUtil.copyProperties(registerDsInstance, DatasourceInstance.class);
        datasourceInstance.setUuid(IdUtil.buildUUID());
        dsInstancService.add(datasourceInstance);
    }
}
