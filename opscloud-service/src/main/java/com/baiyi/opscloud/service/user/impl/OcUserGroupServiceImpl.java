package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserGroup;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.mapper.opscloud.OcUserGroupMapper;
import com.baiyi.opscloud.service.user.OcUserGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:59 下午
 * @Version 1.0
 */
@Service
public class OcUserGroupServiceImpl implements OcUserGroupService {

    @Resource
    private OcUserGroupMapper ocUserGroupMapper;

    @Override
    public DataTable<OcUserGroup> queryOcUserGroupByParam(UserBusinessGroupParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcUserGroup> ocUserGroupList = ocUserGroupMapper.queryOcUserGroupByParam(pageQuery);
        return new DataTable<>(ocUserGroupList, page.getTotal());
    }

    @Override
    public DataTable<OcUserGroup> queryUserIncludeOcUserGroupByParam(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcUserGroup> ocUserGroupList = ocUserGroupMapper.queryUserOcUserGroupByParam(pageQuery);
        return new DataTable<>(ocUserGroupList, page.getTotal());
    }

    @Override
    public DataTable<OcUserGroup> queryUserExcludeOcUserGroupByParam(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcUserGroup> ocUserGroupList = ocUserGroupMapper.queryUserExcludeOcUserGroupByParam(pageQuery);
        return new DataTable<>(ocUserGroupList, page.getTotal());
    }

    @Override
    public void addOcUserGroup(OcUserGroup ocUserGroup) {
        ocUserGroupMapper.insert(ocUserGroup);
    }

    @Override
    public void updateOcUserGroup(OcUserGroup ocUserGroup) {
        ocUserGroupMapper.updateByPrimaryKey(ocUserGroup);
    }

    @Override
    public OcUserGroup queryOcUserGroupByName(String name) {
        Example example = new Example(OcUserGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return ocUserGroupMapper.selectOneByExample(example);
    }

    @Override
    public OcUserGroup queryOcUserGroupById(int id) {
        return ocUserGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcUserGroup> queryOcUserGroupByUserId(int userId) {
        return ocUserGroupMapper.queryOcUserGroupByUserId(userId);
    }

    @Override
    public List<OcUserGroup> queryUserTicketOcUserGroupByParam(UserBusinessGroupParam.UserTicketOcUserGroupQuery queryParam) {
        return ocUserGroupMapper.queryUserTicketOcUserGroupByParam(queryParam);
    }
}
