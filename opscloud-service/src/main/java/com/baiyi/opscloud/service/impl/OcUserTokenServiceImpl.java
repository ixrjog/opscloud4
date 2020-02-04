package com.baiyi.opscloud.service.impl;

import com.baiyi.opscloud.domain.generator.OcUserToken;
import com.baiyi.opscloud.mapper.OcUserTokenMapper;
import com.baiyi.opscloud.service.OcUserTokenService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:22 上午
 * @Version 1.0
 */
@Service
public class OcUserTokenServiceImpl implements OcUserTokenService {

    @Resource
    private OcUserTokenMapper ocUserTokenMapper;

    @Override
    public void addOcUserToken(OcUserToken ocUserToken) {
        ocUserTokenMapper.insert(ocUserToken);
    }

    @Override
    public OcUserToken queryOcUserTokenByTokenAndValid(String token) {
        Example example = new Example( OcUserToken .class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        criteria.andEqualTo("token", token);
        return ocUserTokenMapper.selectOneByExample(example);
    }



}
