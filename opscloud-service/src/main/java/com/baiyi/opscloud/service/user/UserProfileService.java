package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.UserProfile;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/2/2 14:56
 * @Version 1.0
 */
public interface UserProfileService {

    void add(UserProfile userProfile);

    void updateByPrimaryKeySelective(UserProfile userProfile);

    List<UserProfile> queryByUsername(String username);

}