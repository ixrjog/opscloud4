package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthRole;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.mapper.opscloud.OcAuthRoleMapper;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:12 下午
 * @Version 1.0
 */
@Service
public class OcAuthRoleServiceImpl implements OcAuthRoleService {

    @Resource
    private OcAuthRoleMapper ocAuthRoleMapper;

    @Override
    public DataTable<OcAuthRole> queryOcAuthRoleByParam(RoleParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcAuthRole> ocAuthRoleList = ocAuthRoleMapper.queryOcAuthRoleByParam(pageQuery);
        return new DataTable<>(ocAuthRoleList, page.getTotal());
    }

    @Override
    public OcAuthRole queryTopOcAuthRoleByUsername(String username) {
        return ocAuthRoleMapper.queryTopOcAuthRoleByUsername(username);
    }


    @Override
    public int queryOcAuthRoleAccessLevelByUsername(String username) {
        return ocAuthRoleMapper.queryOcAuthRoleAccessLevelByUsername(username);
    }

    @Override
    public void addOcAuthRole(OcAuthRole ocAuthRole) {
        ocAuthRoleMapper.insert(ocAuthRole);
    }

    @Override
    public void updateOcAuthRole(OcAuthRole ocAuthRole) {
        ocAuthRoleMapper.updateByPrimaryKey(ocAuthRole);
    }

    @Override
    public void deleteOcAuthRoleById(int id) {
        ocAuthRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcAuthRole queryOcAuthRoleById(int id) {
        return ocAuthRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcAuthRole queryOcAuthRoleByName(String roleName) {
        Example example = new Example(OcAuthRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleName", roleName);
        return ocAuthRoleMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAuthRole> queryUserTicketOcAuthRoleByParam(RoleParam.UserTicketOcAuthRoleQuery queryParam) {
        return ocAuthRoleMapper.queryUserTicketOcAuthRoleByParam(queryParam);
    }

    @Override
    public List<OcAuthRole> queryAllOcAuthRole() {
        return ocAuthRoleMapper.selectAll();
    }
}
