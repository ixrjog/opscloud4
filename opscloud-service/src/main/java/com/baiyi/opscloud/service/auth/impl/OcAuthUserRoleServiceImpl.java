package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthUserRole;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;
import com.baiyi.opscloud.mapper.opscloud.OcAuthUserRoleMapper;
import com.baiyi.opscloud.service.auth.OcAuthUserRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/12 3:21 下午
 * @Version 1.0
 */
@Service
public class OcAuthUserRoleServiceImpl implements OcAuthUserRoleService {

    @Resource
    private OcAuthUserRoleMapper ocAuthUserRoleMapper;

    @Override
    public int countByRoleId(int roleId) {
        Example example = new Example(OcAuthUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return ocAuthUserRoleMapper.selectCountByExample(example);
    }

    @Override
    public DataTable<OcAuthUserRole> queryOcAuthUserRoleByParam(UserRoleParam.UserRolePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAuthUserRole> ocAuthUserRoleList = ocAuthUserRoleMapper.queryOcAuthUserRoleByParam(pageQuery);
        return new DataTable<>(ocAuthUserRoleList, page.getTotal());
    }

    @Override
    public List<OcAuthUserRole> queryOcAuthUserRolesByUsername(String username) {
        Example example = new Example(OcAuthUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return ocAuthUserRoleMapper.selectByExample(example);
    }

    @Override
    public void addOcAuthUserRole(OcAuthUserRole ocAuthUserRole) {
        ocAuthUserRoleMapper.insert(ocAuthUserRole);
    }

    @Override
    public void deleteOcAuthUserRoleById(int id) {
        ocAuthUserRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcAuthUserRole queryOcAuthUserRoleById(int id) {
        return ocAuthUserRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcAuthUserRole queryOcAuthUserRoleByUniqueKey(OcAuthUserRole ocAuthUserRole) {
        Example example = new Example(OcAuthUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", ocAuthUserRole.getRoleId());
        criteria.andEqualTo("username", ocAuthUserRole.getUsername());
        return ocAuthUserRoleMapper.selectOneByExample(example);
    }

    @Override
    public boolean authenticationByUsernameAndResourceName(String username, String resourceName) {
        return ocAuthUserRoleMapper.authenticationByUsernameAndResourceName(username, resourceName) >= 1;
    }
}
