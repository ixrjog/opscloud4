package com.baiyi.opscloud.service.org.impl;

import com.baiyi.opscloud.domain.generator.OcOrgDepartment;
import com.baiyi.opscloud.mapper.opscloud.OcOrgDepartmentMapper;
import com.baiyi.opscloud.service.org.OcOrgDepartmentService;
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
    public void updateOcOrgDepartment(OcOrgDepartment ocOrgDepartment) {
        ocOrgDepartmentMapper.updateByPrimaryKey(ocOrgDepartment);
    }

}
