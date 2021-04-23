package com.baiyi.opscloud.service.menu.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcRoleMenu;
import com.baiyi.opscloud.mapper.opscloud.OcRoleMenuMapper;
import com.baiyi.opscloud.service.menu.OcRoleMenuService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/2 3:30 下午
 * @Since 1.0
 */

@Service
public class OcRoleMenuServiceImpl implements OcRoleMenuService {

    @Resource
    private OcRoleMenuMapper ocRoleMenuMapper;

    @Override
    public void addOcRoleMenuList(List<OcRoleMenu> ocRoleMenuList) {
        ocRoleMenuMapper.insertList(ocRoleMenuList);
    }

    @Override
    public void delOcRoleMenuByRoleId(Integer roleId) {
        Example example = new Example(OcRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        ocRoleMenuMapper.deleteByExample(example);
    }

    @Override
    public List<OcRoleMenu> queryOcRoleMenuByRoleIdList(List<Integer> roleIdList) {
        return ocRoleMenuMapper.queryOcRoleMenuByRoleIdList(roleIdList);
    }

    @Override
    public List<OcRoleMenu> queryOcRoleMenuByRoleId(Integer roleId) {
        Example example = new Example(OcRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return ocRoleMenuMapper.selectByExample(example);
    }
}
