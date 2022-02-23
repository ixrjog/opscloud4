package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.annotation.LaterWrapper;
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
    public void wrap(AccessTokenVO.AccessToken accessToken, IExtend iExtend) {
        accessToken.setToken("******");
    }

    /**
     * @param accessToken
     * @param isSecret    是否脱敏
     */
    @LaterWrapper
    public void wrap(AccessTokenVO.AccessToken accessToken, boolean isSecret) {
        if (isSecret) {
            accessToken.setToken("******");
        }
    }

}
