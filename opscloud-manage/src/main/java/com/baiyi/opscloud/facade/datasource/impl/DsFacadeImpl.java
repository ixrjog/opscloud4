package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.datasource.DsConfigParam;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.vo.datasource.DsConfigVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.facade.datasource.DsFacade;
import com.baiyi.opscloud.packer.datasource.DsConfigPacker;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:35 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DsFacadeImpl implements DsFacade {

    private final DsConfigService dsConfigService;

    private final DsInstanceService dsInstanceService;

    private final DsConfigPacker dsConfigPacker;

    private final DsInstancePacker dsInstancePacker;

    @Override
    public void setDsInstanceConfig(int instanceId) {
    }

    @Override
    public DataTable<DsConfigVO.DsConfig> queryDsConfigPage(DsConfigParam.DsConfigPageQuery pageQuery) {
        DataTable<DatasourceConfig> table = dsConfigService.queryPageByParam(pageQuery);
        List<DsConfigVO.DsConfig> data = BeanCopierUtil.copyListProperties(table.getData(), DsConfigVO.DsConfig.class).stream()
                .peek(e -> dsConfigPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addDsConfig(DsInstanceParam.AddDsConfig dsConfig) {
        DatasourceConfig datasourceConfig = BeanCopierUtil.copyProperties(dsConfig, DatasourceConfig.class);
        datasourceConfig.setUuid(IdUtil.buildUUID());
        dsConfigService.add(datasourceConfig);
    }

    @Override
    public void updateDsConfig(DsInstanceParam.UpdateDsConfig dsConfig) {
        dsConfigService.update(BeanCopierUtil.copyProperties(dsConfig, DatasourceConfig.class));
    }

    @Override
    public List<DsInstanceVO.Instance> queryDsInstance(DsInstanceParam.DsInstanceQuery query) {
        List<DatasourceInstance> instances = dsInstanceService.queryByParam(query);
        return BeanCopierUtil.copyListProperties(instances, DsInstanceVO.Instance.class)
                .stream().peek(e -> dsInstancePacker.wrap(e, query)).collect(Collectors.toList());
    }

    @Override
    public void registerDsInstance(DsInstanceParam.RegisterDsInstance registerDsInstance) {
        DatasourceInstance datasourceInstance = BeanCopierUtil.copyProperties(registerDsInstance, DatasourceInstance.class);
        if (datasourceInstance.getId() == null) {
            datasourceInstance.setUuid(IdUtil.buildUUID());
            dsInstanceService.add(datasourceInstance);
        } else {
            dsInstanceService.update(datasourceInstance);
        }
    }

    @Override
    public DsInstanceVO.Instance queryDsInstanceById(int instanceId) {
        DatasourceInstance instance = dsInstanceService.getById(instanceId);
        return BeanCopierUtil.copyProperties(instance, DsInstanceVO.Instance.class);
    }

    @Override
    public DsConfigVO.DsConfig queryDsConfigById(int configId) {
        DatasourceConfig config = dsConfigService.getById(configId);
        DsConfigVO.DsConfig result = BeanCopierUtil.copyProperties(config, DsConfigVO.DsConfig.class);
        dsConfigPacker.wrap(result, SimpleExtend.EXTEND);
        return result;
    }

}
