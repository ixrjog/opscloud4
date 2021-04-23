package com.baiyi.opscloud.service.org.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.mapper.opscloud.OcOrgDepartmentMemberMapper;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:25 下午
 * @Version 1.0
 */
@Service
public class OcOrgDepartmentMemberServiceImpl implements OcOrgDepartmentMemberService {

    @Resource
    private OcOrgDepartmentMemberMapper ocOrgDepartmentMemberMapper;

    @Override
    public DataTable<OcOrgDepartmentMember> queryOcOrgDepartmentMemberParam(DepartmentMemberParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcOrgDepartmentMember> list = ocOrgDepartmentMemberMapper.queryOcOrgDepartmentMemberParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcOrgDepartmentMember(OcOrgDepartmentMember ocOrgDepartmentMember) {
        ocOrgDepartmentMemberMapper.insert(ocOrgDepartmentMember);
    }

    @Override
    public void updateOcOrgDepartmentMember(OcOrgDepartmentMember ocOrgDepartmentMember) {
        ocOrgDepartmentMemberMapper.updateByPrimaryKey(ocOrgDepartmentMember);
    }

    @Override
    public OcOrgDepartmentMember getOcOrgDepartmentMemberByUniqueKey(int departmentId, int userId) {
        Example example = new Example(OcOrgDepartmentMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("departmentId", departmentId);
        criteria.andEqualTo("userId", userId);
        return ocOrgDepartmentMemberMapper.selectOneByExample(example);
    }

    @Override
    public int countOcOrgDepartmentMemberByDepartmentId(int departmentId) {
        Example example = new Example(OcOrgDepartmentMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("departmentId", departmentId);
        return ocOrgDepartmentMemberMapper.selectCountByExample(example);
    }

    @Override
    public List<OcOrgDepartmentMember> queryOcOrgDepartmentMemberByDepartmentId(int departmentId) {
        Example example = new Example(OcOrgDepartmentMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("departmentId", departmentId);
        return ocOrgDepartmentMemberMapper.selectByExample(example);
    }

    @Override
    public OcOrgDepartmentMember queryOcOrgDepartmentMemberByLeader(int departmentId) {
        Example example = new Example(OcOrgDepartmentMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("departmentId", departmentId);
        criteria.andEqualTo("isLeader", 1);
        PageHelper.startPage(1, 1);
        return ocOrgDepartmentMemberMapper.selectOneByExample(example);
    }

    @Override
    public OcOrgDepartmentMember queryOcOrgDepartmentMemberById(int id) {
        return ocOrgDepartmentMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcOrgDepartmentMember> queryOcOrgDepartmentMemberByUserId(int userId) {
        Example example = new Example(OcOrgDepartmentMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return ocOrgDepartmentMemberMapper.selectByExample(example);
    }

    @Override
    public void deleteOcOrgDepartmentMemberById(int id) {
        ocOrgDepartmentMemberMapper.deleteByPrimaryKey(id);
    }
}
