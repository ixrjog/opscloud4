package com.baiyi.caesar.factory;

import com.baiyi.caesar.common.datasource.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.common.util.DsUtil;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.Credential;
import com.baiyi.caesar.service.sys.CredentialService;
import com.baiyi.caesar.util.TemplateUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:31 下午
 * @Version 1.0
 */
@Component
public class DsFactory {

    @Resource
    private TemplateUtil templateUtil;

    @Resource
    private CredentialService credentialService;

    public BaseDsInstanceConfig build(DatasourceConfig datasourceConfig, Class<LdapDsInstanceConfig> targetClass) {
        String propsYml = datasourceConfig.getPropsYml();
        if (!IdUtil.isEmpty(datasourceConfig.getCredentialId())) {
            Credential credential = credentialService.getById(datasourceConfig.getCredentialId());
            if (credential != null)
                propsYml = templateUtil.renderTemplate(propsYml, credential);
        }
        return DsUtil.toDatasourceConfig(propsYml, targetClass);
    }


}
