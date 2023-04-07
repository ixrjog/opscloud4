package com.baiyi.opscloud.core.factory;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.DsUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.core.util.TemplateUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:31 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DsConfigHelper {

    private final TemplateUtil templateUtil;

    private final CredentialService credentialService;

    private final DsInstanceService dsInstanceService;

    private final DsConfigService dsConfigService;

    public DatasourceConfig getConfigByInstanceUuid(String uuid) {
        DatasourceInstance instance = dsInstanceService.getByUuid(uuid);
        return getConfigById(instance.getConfigId());
    }

    public DatasourceConfig getConfigById(Integer id) {
        return dsConfigService.getById(id);
    }

    public DatasourceConfig getConfigByDsType(int dsType) {
        List<DatasourceConfig> configs = dsConfigService.queryByDsType(dsType);
        if (CollectionUtils.isEmpty(configs)) {
            throw new OCException("无可用的数据源配置文件！");
        }
        return configs.get(0);
    }

    public <T extends BaseDsConfig> T build(DatasourceConfig datasourceConfig, Class<T> targetClass) {
        String propsYml = datasourceConfig.getPropsYml();
        if (!IdUtil.isEmpty(datasourceConfig.getCredentialId())) {
            Credential credential = credentialService.getById(datasourceConfig.getCredentialId());
            if (credential != null) {
                propsYml = templateUtil.renderTemplate(propsYml, credential);
            }
        }
        return DsUtil.toDsConfig(propsYml, targetClass);
    }

    public KubernetesConfig buildKubernetesConfig(String instanceUuid) {
        DatasourceInstance instance = dsInstanceService.getByUuid(instanceUuid);
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getConfigId());
        return build(datasourceConfig, KubernetesConfig.class);
    }

}
