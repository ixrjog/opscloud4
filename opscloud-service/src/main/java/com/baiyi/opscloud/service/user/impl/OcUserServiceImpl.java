package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.mapper.opscloud.OcUserMapper;
import com.baiyi.opscloud.service.user.OcUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
    public void updateBaseOcUser(OcUser ocUser) {
        ocUserMapper.updateBaseUser(ocUser);
    }

    @Override
    public OcUser queryOcUserByUsername(String username) {
        return ocUserMapper.queryByUsername(username);
    }

    @Override
    public List<OcUser> queryOcUserByDisplayName(String displayName) {
        Example example = new Example(OcUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("displayName", displayName);
        return ocUserMapper.selectByExample(example);
    }

    @Override
    public void delOcUserByUsername(String username) {
        OcUser ocUser = queryOcUserByUsername(username);
        if (ocUser != null)
            ocUserMapper.deleteByPrimaryKey(ocUser.getId());
    }

    @Override
    public DataTable<OcUser> queryOcUserByParam(UserParam.UserPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcUser> ocUserList = ocUserMapper.queryOcUserByParam(pageQuery);
        return new DataTable<>(ocUserList, page.getTotal());
    }

    @Override
    public DataTable<OcUser> fuzzyQueryUserByParam(UserParam.UserPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcUser> ocUserList = ocUserMapper.fuzzyQueryUserByParam(pageQuery);
        return new DataTable<>(ocUserList, page.getTotal());
    }

    @Override
    public List<OcUser> queryOcUserByUserGroupId(int userGroupId) {
        return ocUserMapper.queryOcUserByUserGroupId(userGroupId);
    }

    @Override
    public List<OcUser> queryOcUserActive() {
        Example example = new Example(OcUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", 1);
        return ocUserMapper.selectByExample(example);
    }

    @Override
    public List<OcUser> queryOcUserInActive() {
        Example example = new Example(OcUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", false);
        return ocUserMapper.selectByExample(example);
    }

    @Override
    public List<OcUser> queryOcUserAll() {
        return ocUserMapper.selectAll();
    }

    @Override
    public List<OcUser> queryOcUserByIdList(List<Integer> userIdList) {
        return ocUserMapper.queryOcUserByIdList(userIdList);
    }

    @Override
    public List<OcUser> queryOcUserByPhone(String phone) {
        Example example = new Example(OcUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("phone", phone);
        return ocUserMapper.selectByExample(example);
    }
}
