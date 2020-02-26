package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.OcUserApiToken;
import com.baiyi.opscloud.mapper.OcUserApiTokenMapper;
import com.baiyi.opscloud.service.user.OcUserApiTokenService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/26 5:34 下午
 * @Version 1.0
 */
@Service
public class OcUserApiTokenServiceImpl implements OcUserApiTokenService {

    @Resource
    private OcUserApiTokenMapper ocUserApiTokenMapper;

    @Override
    public OcUserApiToken queryOcUserApiTokenByTokenAndValid(String token) {
        Example example = new Example(OcUserApiToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        criteria.andEqualTo("token", token);
        return ocUserApiTokenMapper.selectOneByExample(example);
    }

    @Override
    public void updateOcUserApiToken(OcUserApiToken ocUserApiToken) {
        ocUserApiTokenMapper.updateByPrimaryKey(ocUserApiToken);
    }

    @Override
    public int checkUserHasResourceAuthorize(String token, String resourceName) {
        return ocUserApiTokenMapper.checkUserHasResourceAuthorize(token, resourceName);
    }
}
