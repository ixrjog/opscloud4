package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.opscloud.UserProfile;
import com.baiyi.opscloud.mapper.UserProfileMapper;
import com.baiyi.opscloud.service.user.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/2/2 14:56
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileMapper userProfileMapper;

    @Override
    public void add(UserProfile userProfile) {
        userProfileMapper.insert(userProfile);
    }

    @Override
    public void updateByPrimaryKeySelective(UserProfile userProfile) {
        userProfileMapper.updateByPrimaryKeySelective(userProfile);
    }

    @Override
    public List<UserProfile> queryByUsername(String username) {
        Example example = new Example(UserProfile.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return userProfileMapper.selectByExample(example);
    }

}