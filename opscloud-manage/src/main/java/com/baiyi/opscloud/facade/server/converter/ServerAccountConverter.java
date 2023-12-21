package com.baiyi.opscloud.facade.server.converter;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.param.server.ServerAccountParam;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/12/15 13:52
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class ServerAccountConverter {

    private final CredentialService credentialService;

    public ServerAccount to(ServerAccountParam.ServerAccount account) {
        ServerAccount serverAccount = BeanCopierUtil.copyProperties(account, ServerAccount.class);
        if (StringUtils.isEmpty(serverAccount.getUsername())) {
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            serverAccount.setUsername(credential.getUsername());
        }
        return serverAccount;
    }

}