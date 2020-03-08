package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcUserCredential;
import com.baiyi.opscloud.mapper.opscloud.OcUserCredentialMapper;
import com.baiyi.opscloud.service.user.OcUserCredentialService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/27 6:41 下午
 * @Version 1.0
 */
@Service
public class OcUserCredentialServiceImpl implements OcUserCredentialService {

    @Resource
    private OcUserCredentialMapper ocUserCredentialMapper;

    @Override
    public OcUserCredential queryOcUserCredentialByUniqueKey(OcUserCredential ocUserCredential) {
        Example example = new Example(OcUserCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", ocUserCredential.getUserId());
        criteria.andEqualTo("credentialType", ocUserCredential.getCredentialType());
        return ocUserCredentialMapper.selectOneByExample(example);
    }

    @Override
    public void addOcUserCredential(OcUserCredential ocUserCredential) {
        ocUserCredentialMapper.insert(ocUserCredential);
    }

    @Override
    public void updateOcUserCredential(OcUserCredential ocUserCredential) {
        ocUserCredentialMapper.updateByPrimaryKey(ocUserCredential);
    }

    @Override
    public List<OcUserCredential> queryOcUserCredentialByUserId(int userId) {
        Example example = new Example(OcUserCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return ocUserCredentialMapper.selectByExample(example);
    }
}
