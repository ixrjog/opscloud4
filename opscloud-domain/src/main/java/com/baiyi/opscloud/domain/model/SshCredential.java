package com.baiyi.opscloud.domain.model;

import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:20 下午
 * @Version 1.0
 */
@Data
@Builder
public class SshCredential {

    private Credential credential;

    private ServerAccount serverAccount;

}