package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.annotation.LaterWrapper;
import com.baiyi.opscloud.domain.annotation.DesensitizedMethod;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.user.AccessTokenVO;
import com.baiyi.opscloud.packer.IWrapper;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/5 10:27 上午
 * @Version 1.0
 */
@Component
public class UserAccessTokenPacker implements IWrapper<AccessTokenVO.AccessToken> {

    @Override
    @LaterWrapper
    @DesensitizedMethod
    public void wrap(AccessTokenVO.AccessToken accessToken, IExtend iExtend) {
    }
}