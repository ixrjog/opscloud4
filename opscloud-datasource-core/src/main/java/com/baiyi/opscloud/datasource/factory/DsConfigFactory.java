package com.baiyi.opscloud.datasource.factory;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.util.DsUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.datasource.util.TemplateUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.sys.CredentialService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:31 下午
 * @Version 1.0
 */
@Component
public class DsConfigFactory {

    @Resource
    private TemplateUtil templateUtil;

    @Resource
    private CredentialService credentialService;

    public <T extends BaseDsInstanceConfig> T build(DatasourceConfig datasourceConfig, Class<T> targetClass) {
        String propsYml = datasourceConfig.getPropsYml();
        if (!IdUtil.isEmpty(datasourceConfig.getCredentialId())) {
            Credential credential = credentialService.getById(datasourceConfig.getCredentialId());
            if (credential != null)
                propsYml = templateUtil.renderTemplate(propsYml, credential);
        }
        return DsUtil.toDatasourceConfig(propsYml, targetClass);
    }


}
