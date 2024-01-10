package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AccessToken;
import com.baiyi.opscloud.mapper.AccessTokenMapper;
import com.baiyi.opscloud.service.user.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/5 9:47 上午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final AccessTokenMapper accessTokenMapper;

    @Override
    public void add(AccessToken accessToken) {
        accessTokenMapper.insert(accessToken);
    }

    @Override
    public void update(AccessToken accessToken) {
        accessTokenMapper.updateByPrimaryKey(accessToken);
    }

    @Override
    public AccessToken getByToken(String token) {
        Example example = new Example(AccessToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("token", token);
        criteria.andEqualTo("valid", true);
        return accessTokenMapper.selectOneByExample(example);
    }

    @Override
    public AccessToken getByTokenId(String tokenId) {
        Example example = new Example(AccessToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tokenId", tokenId);
        criteria.andEqualTo("valid", true);
        return accessTokenMapper.selectOneByExample(example);
    }

    @Override
    public List<AccessToken> queryByUsername(String username) {
        Example example = new Example(AccessToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("valid", true);
        return accessTokenMapper.selectByExample(example);
    }

    @Override
    public int checkUserHasResourceAuthorize(String accessToken, String resourceName) {
        return accessTokenMapper.checkUserHasResourceAuthorize(accessToken, resourceName);
    }

}