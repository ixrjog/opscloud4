package com.baiyi.caesar.facade.datasource.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.param.datasource.DsConfigParam;
import com.baiyi.caesar.domain.param.datasource.DsInstanceParam;
import com.baiyi.caesar.domain.vo.datasource.DsConfigVO;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;
import com.baiyi.caesar.facade.datasource.DsFacade;
import com.baiyi.caesar.packer.datasource.DsConfigPacker;
import com.baiyi.caesar.packer.datasource.DsInstancePacker;
import com.baiyi.caesar.service.datasource.DsConfigService;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:35 下午
 * @Version 1.0
 */
@Service
public class DsFacadeImpl implements DsFacade {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsInstanceService dsInstancService;

    @Resource
    private DsConfigPacker dsConfigPacker;

    @Resource
    private DsInstancePacker dsInstancePacker;

    @Override
    public void setDsInstanceConfig(int instanceId) {

    }

    @Override
    public DataTable<DsConfigVO.DsConfig> queryDsConfigPage(DsConfigParam.DsConfigPageQuery pageQuery) {
        DataTable<DatasourceConfig> table = dsConfigService.queryPageByParam(pageQuery);
        return new DataTable<>(dsConfigPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void addDsConfig(DsConfigVO.DsConfig dsConfig) {
        DatasourceConfig datasourceConfig = BeanCopierUtil.copyProperties(dsConfig, DatasourceConfig.class);
        datasourceConfig.setUuid(IdUtil.buildUUID());
        dsConfigService.add(datasourceConfig);
    }

    @Override
    public void updateDsConfig(DsConfigVO.DsConfig dsConfig) {
        dsConfigService.update(BeanCopierUtil.copyProperties(dsConfig, DatasourceConfig.class));
    }

    @Override
    public List<DsInstanceVO.Instance> queryDsInstance(DsInstanceParam.DsInstanceQuery query) {
        List<DatasourceInstance> instanceList = dsInstancService.queryByParam(query);
        return dsInstancePacker.wrapVOList(instanceList, query);
    }

    @Override
    public void registerDsInstance(DsInstanceParam.RegisterDsInstance registerDsInstance) {
        DatasourceInstance datasourceInstance = BeanCopierUtil.copyProperties(registerDsInstance, DatasourceInstance.class);
        datasourceInstance.setUuid(IdUtil.buildUUID());
        dsInstancService.add(datasourceInstance);
    }
}
