package com.baiyi.opscloud.service.org.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import com.baiyi.opscloud.mapper.opscloud.OcOrgDepartmentMapper;
import com.baiyi.opscloud.service.org.OcOrgDepartmentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.var;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:42 下午
 * @Version 1.0
 */
@Service
public class OcOrgDepartmentServiceImpl implements OcOrgDepartmentService {

    @Resource
    private OcOrgDepartmentMapper ocOrgDepartmentMapper;

    @Override
    public DataTable<OcOrgDepartment> queryOcOrgDepartmentParam(DepartmentParam.PageQuery pageQuery) {
        var page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcOrgDepartment> list = ocOrgDepartmentMapper.queryOcOrgDepartmentParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcOrgDepartment> queryOcOrgDepartmentByParentId(int parentId) {
        Example example = new Example(OcOrgDepartment.class);
        example.setOrderByClause("dept_order");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        return ocOrgDepartmentMapper.selectByExample(example);
    }

    @Override
    public OcOrgDepartment queryOcOrgDepartmentById(int id) {
        return ocOrgDepartmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcOrgDepartment(OcOrgDepartment ocOrgDepartment) {
        ocOrgDepartmentMapper.insert(ocOrgDepartment);
    }

    @Override
    public void updateOcOrgDepartment(OcOrgDepartment ocOrgDepartment) {
        ocOrgDepartmentMapper.updateByPrimaryKey(ocOrgDepartment);
    }

    @Override
    public void deleteOcOrgDepartmentById(int id) {
        ocOrgDepartmentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcOrgDepartment> queryOcOrgDepartmentByIdList(List<Integer> idList) {
        return ocOrgDepartmentMapper.queryOcOrgDepartmentByIdList(idList);
    }

    @Override
    public DataTable<OcOrgDepartment> queryFirstLevelDepartmentPage(DepartmentParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcOrgDepartment> list = ocOrgDepartmentMapper.queryFirstLevelDepartmentPage(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcOrgDepartment> queryOcOrgDepartmentAll() {
        return ocOrgDepartmentMapper.selectAll();
    }
}
