package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.mapper.UserCredentialMapper;
import com.baiyi.opscloud.service.user.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/9 10:04 上午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialMapper userCredentialMapper;

    @Override
    public List<UserCredential> queryByUserIdAndType(Integer userId, int credentialType) {
        Example example = new Example(UserCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("credentialType", credentialType);
        return userCredentialMapper.selectByExample(example);
    }

    @Override
    public int countByUserIdAndType(Integer userId, int credentialType) {
        Example example = new Example(UserCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("credentialType", credentialType);
        return userCredentialMapper.selectCountByExample(example);
    }

    @Override
    public List<UserCredential> queryByUserId(Integer userId) {
        Example example = new Example(UserCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return userCredentialMapper.selectByExample(example);
    }

    @Override
    public UserCredential getById(Integer id) {
        return userCredentialMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(UserCredential userCredential) {
        userCredentialMapper.insert(userCredential);
    }

    @Override
    public void update(UserCredential userCredential) {
        userCredentialMapper.updateByPrimaryKey(userCredential);
    }

    @Override
    public void deleteById(int id) {
        userCredentialMapper.deleteByPrimaryKey(id);
    }

}