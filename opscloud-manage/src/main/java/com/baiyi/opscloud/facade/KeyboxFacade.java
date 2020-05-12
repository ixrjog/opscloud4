package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.bo.SSHKeyCredential;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:17 下午
 * @Version 1.0
 */
public interface KeyboxFacade {

    SSHKeyCredential getSSHKeyCredential(String systemUser);
}
