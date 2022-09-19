package com.baiyi.opscloud.packer.auth;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.auth.AuthPlatformVO;
import com.baiyi.opscloud.packer.IWrapper;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/9/19 10:46
 * @Version 1.0
 */
@Component
public class AuthPlatformLogPacker implements IWrapper<AuthPlatformVO.AuthPlatformLog> {

    @Override
    @AgoWrapper
    public void wrap(AuthPlatformVO.AuthPlatformLog authPlatformLog, IExtend iExtend) {
    }

}

