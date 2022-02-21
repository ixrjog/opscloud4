package com.baiyi.opscloud.packer.user.delegate;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.auth.AuthRolePacker;
import com.baiyi.opscloud.packer.user.UserAccessTokenPacker;
import com.baiyi.opscloud.packer.user.UserCredentialPacker;
import com.baiyi.opscloud.packer.user.am.AmPacker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/9 2:47 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPackerDelegate {

    private final AuthRolePacker authRolePacker;

    private final UserCredentialPacker userCredentialPacker;

    private final UserAccessTokenPacker userAccessTokenPacker;

    private final AmPacker amPacker;

    @TagsWrapper
    public void wrap(UserVO.User user, IExtend iExtend) {
        authRolePacker.wrap(user);
        userCredentialPacker.wrap(user);
        userAccessTokenPacker.wrap(user);
        amPacker.wrap(user);
    }

}
