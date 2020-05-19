package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthMenu;
import com.baiyi.opscloud.mapper.opscloud.OcAuthMenuMapper;
import com.baiyi.opscloud.service.auth.OcAuthMenuService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:27 上午
 * @Version 1.0
 */
@Service
public class OcAuthMenuServiceImpl implements OcAuthMenuService {

    @Resource
    private OcAuthMenuMapper ocAuthMenuMapper;

    @Override
    public OcAuthMenu queryOcAuthMenuByRoleId(int roleId, int menuType) {
        Example example = new Example(OcAuthMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        criteria.andEqualTo("menuType", menuType);
        return ocAuthMenuMapper.selectOneByExample(example);
    }

    @Override
    public void addOcAuthMenu(OcAuthMenu ocAuthMenu) {
        ocAuthMenuMapper.insert(ocAuthMenu);
    }

    @Override
    public void updateOcAuthMenu(OcAuthMenu ocAuthMenu) {
        ocAuthMenuMapper.updateByPrimaryKey(ocAuthMenu);
    }

    @Override
    public void deletetOcAuthMenuById(OcAuthMenu ocAuthMenu) {
        ocAuthMenuMapper.deleteByPrimaryKey(ocAuthMenu);
    }
}
