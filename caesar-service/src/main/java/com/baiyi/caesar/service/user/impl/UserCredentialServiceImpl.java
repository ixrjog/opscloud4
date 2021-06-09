package com.baiyi.caesar.service.user.impl;

import com.baiyi.caesar.domain.generator.caesar.UserCredential;
import com.baiyi.caesar.mapper.caesar.UserCredentialMapper;
import com.baiyi.caesar.service.user.UserCredentialService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/9 10:04 上午
 * @Version 1.0
 */
@Service
public class UserCredentialServiceImpl implements UserCredentialService {

    @Resource
    private UserCredentialMapper userCredentialMapper;

    @Override
    public List<UserCredential> queryByUserIdAndType(Integer userId, int credentialType) {
        Example example = new Example(UserCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("credentialType", credentialType);
        return userCredentialMapper.selectByExample(example);
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

}
