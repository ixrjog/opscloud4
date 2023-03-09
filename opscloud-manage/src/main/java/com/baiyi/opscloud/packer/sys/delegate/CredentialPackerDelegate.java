package com.baiyi.opscloud.packer.sys.delegate;

import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.annotation.DesensitizedMethod;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import com.baiyi.opscloud.factory.credential.CredentialCustomerFactory;
import com.baiyi.opscloud.packer.IWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/24 10:37 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CredentialPackerDelegate implements IWrapper<CredentialVO.Credential> {

    @Override
    @DesensitizedMethod
    public void wrap(CredentialVO.Credential credential, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        credential.setQuantityUsed(CredentialCustomerFactory.countByCredentialId(credential.getId()));
    }

}
