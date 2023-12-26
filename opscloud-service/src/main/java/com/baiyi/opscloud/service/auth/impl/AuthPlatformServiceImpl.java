package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatform;
import com.baiyi.opscloud.factory.credential.AbstractCredentialCustomer;
import com.baiyi.opscloud.mapper.AuthPlatformMapper;
import com.baiyi.opscloud.service.auth.AuthPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/26 11:22
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthPlatformServiceImpl extends AbstractCredentialCustomer implements AuthPlatformService {

    private final AuthPlatformMapper authPlatformMapper;

    @Override
    public AuthPlatform getByName(String name) {
        Example example = new Example(AuthPlatform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("name", name)
                .andEqualTo("isActive", true);
        return authPlatformMapper.selectOneByExample(example);
    }

    @Override
    public List<AuthPlatform> queryAll() {
        Example example = new Example(AuthPlatform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return authPlatformMapper.selectByExample(example);
    }

    @Override
    public int countByCredentialId(int credentialId) {
        Example example = new Example(AuthPlatform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("credentialId", credentialId);
        return authPlatformMapper.selectCountByExample(example);
    }

    @Override
    public String getBeanName() {
        return "AuthPlatformService";
    }

}