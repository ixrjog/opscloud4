package com.baiyi.opscloud.convert;

import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserApiToken;
import com.baiyi.opscloud.domain.vo.user.UserApiTokenVO;

/**
 * @Author baiyi
 * @Date 2020/2/27 6:29 下午
 * @Version 1.0
 */
public class UserApiTokenConvert {

    public static OcUserApiToken convertOcUserApiToken (UserApiTokenVO.UserApiToken userApiToken) {
        OcUserApiToken ocUserApiToken = new OcUserApiToken();
        ocUserApiToken.setValid(true);
        ocUserApiToken.setComment(userApiToken.getComment());
        ocUserApiToken.setUsername(SessionUtils.getUsername());
        ocUserApiToken.setTokenId(PasswordUtils.getRandomPW(20)); // 不含特殊字符
        ocUserApiToken.setToken(PasswordUtils.getPW(20)); //高强度
        ocUserApiToken.setExpiredTime(userApiToken.getExpiredTime());
        return ocUserApiToken;
    }
}
