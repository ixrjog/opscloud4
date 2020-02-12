package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.OcUser;
import com.baiyi.opscloud.mapper.OcUserMapper;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/3 6:51 下午
 * @Version 1.0
 */
@Service
public class OcUserServiceImpl implements OcUserService {

    @Resource
    private OcUserMapper ocUserMapper;

    @Override
    public void addOcUser(OcUser ocUser) {
        ocUserMapper.insert(ocUser);
    }

    @Override
    public OcUser queryOcUserById(Integer id) {
        return ocUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateOcUser(OcUser ocUser) {
        ocUserMapper.updateByPrimaryKey(ocUser);
    }

    @Override
    public OcUser queryOcUserByUsername(String username) {
        return ocUserMapper.queryByUsername(username);
    }

    @Override
    public void delOcUserByUsername(String username) {
        OcUser ocUser = queryOcUserByUsername(username);
        if (ocUser != null)
            ocUserMapper.deleteByPrimaryKey(ocUser.getId());
    }

}
