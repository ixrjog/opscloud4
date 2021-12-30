package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AccessToken;
import com.baiyi.opscloud.domain.vo.user.AccessTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.service.user.AccessTokenService;
import com.baiyi.opscloud.common.util.time.LaterUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/5 10:27 上午
 * @Version 1.0
 */
@Component
public class UserAccessTokenPacker {

    @Resource
    private AccessTokenService accessTokenService;

    public void wrap(UserVO.User user) {
        List<AccessToken> accessTokens = accessTokenService.queryByUsername(user.getUsername());
        user.setAccessTokens(wrapToVOList(accessTokens));
    }

    public List<AccessTokenVO.AccessToken> wrapToVOList(List<AccessToken> accessTokens) {
        accessTokens = filter(accessTokens);
        if (CollectionUtils.isEmpty(accessTokens))
            return Collections.emptyList();
        return accessTokens.stream().map(e -> wrapToVO(e, true)
        ).collect(Collectors.toList());
    }

    public AccessTokenVO.AccessToken wrapToVO(AccessToken accessToken, boolean isSecret) {
        AccessTokenVO.AccessToken vo = BeanCopierUtil.copyProperties(accessToken, AccessTokenVO.AccessToken.class);
        LaterUtil.wrap(vo);
        if (isSecret)
            vo.setToken("******");
        return vo;
    }

    private List<AccessToken> filter(List<AccessToken> accessTokens) {
        Date now = new Date();
        return accessTokens.stream().filter(a -> a.getExpiredTime().getTime() > now.getTime()).collect(Collectors.toList());
    }

}
