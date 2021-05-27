package com.baiyi.caesar.service.user.impl;

import com.baiyi.caesar.domain.generator.caesar.UserToken;
import com.baiyi.caesar.mapper.caesar.UserTokenMapper;
import com.baiyi.caesar.service.user.UserTokenService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 4:04 下午
 * @Version 1.0
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Resource
    private UserTokenMapper userTokenMapper;

    @Override
    public void add(UserToken userToken) {
        userTokenMapper.insert(userToken);
    }

    @Override
    public void update(UserToken userToken) {
        userTokenMapper.updateByPrimaryKey(userToken);
    }

    @Override
    public int checkUserHasResourceAuthorize(String token, String resourceName) {
        return userTokenMapper.checkUserHasResourceAuthorize(token, resourceName);
    }

    @Override
    public int checkUserHasRole(String token, String roleName) {
        return userTokenMapper.checkUserHasRole(token, roleName);
    }

    @Override
    public UserToken getByVaildToken(String token) {
        Example example = new Example(UserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true)
                .andEqualTo("token", token);
        return userTokenMapper.selectOneByExample(example);
    }

    @Override
    public List<UserToken> queryByVaildTokenByUsername(String username) {
        Example example = new Example(UserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true)
                .andEqualTo("username", username);
        return userTokenMapper.selectByExample(example);
    }
}
