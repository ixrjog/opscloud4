package com.baiyi.opscloud.packer.user.delegate;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AccessToken;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.user.AccessTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.auth.AuthRolePacker;
import com.baiyi.opscloud.packer.user.UserAccessTokenPacker;
import com.baiyi.opscloud.packer.user.UserCredentialPacker;
import com.baiyi.opscloud.packer.user.am.AccessManagementPacker;
import com.baiyi.opscloud.service.user.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private final AccessManagementPacker amPacker;

    private final AccessTokenService accessTokenService;

    @TagsWrapper
    public void wrap(UserVO.User user, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        authRolePacker.wrap(user);
        userCredentialPacker.wrap(user);
        wrap(user);
        amPacker.wrap(user);
    }

    /**
     * Wrapper AccessTokens
     *
     * @param user
     */
    private void wrap(UserVO.User user) {
        List<AccessToken> accessTokens = accessTokenService.queryByUsername(user.getUsername());
        Date now = new Date();
        List<AccessTokenVO.AccessToken> tokens = BeanCopierUtil.copyListProperties(accessTokens, AccessTokenVO.AccessToken.class).stream()
                .filter(e -> e.getExpiredTime().getTime() > now.getTime())
                .peek(e -> userAccessTokenPacker.wrap(e, SimpleExtend.EXTEND))
                .collect(Collectors.toList());
        user.setAccessTokens(tokens);
    }

}
