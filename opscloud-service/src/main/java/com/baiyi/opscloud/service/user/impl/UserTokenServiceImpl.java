package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.common.annotation.ArkIntercept;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.mapper.UserTokenMapper;
import com.baiyi.opscloud.service.user.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 4:04 下午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenMapper userTokenMapper;

    @Override
    public void add(UserToken userToken) {
        userTokenMapper.insert(userToken);
    }

    @Override
    public void update(UserToken userToken) {
        userTokenMapper.updateByPrimaryKey(userToken);
    }

    @Override
    @ArkIntercept
    public int checkUserHasResourceAuthorize(String token, String resourceName) {
        return userTokenMapper.checkUserHasResourceAuthorize(token, resourceName);
    }

    @Override
    @ArkIntercept
    public int checkUserHasRole(String token, String roleName) {
        return userTokenMapper.checkUserHasRole(token, roleName);
    }

    @Override
    public UserToken getByValidToken(String token) {
        Example example = new Example(UserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true)
                .andEqualTo("token", token);
        return userTokenMapper.selectOneByExample(example);
    }

    @Override
    public List<UserToken> queryByValidTokenByUsername(String username) {
        Example example = new Example(UserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true)
                .andEqualTo("username", username);
        return userTokenMapper.selectByExample(example);
    }

}