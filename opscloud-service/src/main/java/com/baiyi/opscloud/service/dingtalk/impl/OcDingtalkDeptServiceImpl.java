package com.baiyi.opscloud.service.dingtalk.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.baiyi.opscloud.mapper.opscloud.OcDingtalkDeptMapper;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkDeptService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 3:07 下午
 * @Since 1.0
 */

@Service
public class OcDingtalkDeptServiceImpl implements OcDingtalkDeptService {

    @Resource
    private OcDingtalkDeptMapper ocDingtalkDeptMapper;

    @Override
    public void addOcDingtalkDept(OcDingtalkDept ocDingtalkDept) {
        ocDingtalkDeptMapper.insert(ocDingtalkDept);
    }

    @Override
    public void updateOcDingtalkDept(OcDingtalkDept ocDingtalkDept) {
        ocDingtalkDeptMapper.updateByPrimaryKey(ocDingtalkDept);
    }

    @Override
    public void deleteOcDingtalkDept(int id) {
        ocDingtalkDeptMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcDingtalkDept> queryOcDingtalkDeptByParentId(Long parentId, String uid) {
        Example example = new Example(OcDingtalkDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        criteria.andEqualTo("dingtalkUid", uid);
        return ocDingtalkDeptMapper.selectByExample(example);
    }

    @Override
    public List<OcDingtalkDept> queryOcDingtalkDeptAll() {
        return ocDingtalkDeptMapper.selectAll();
    }

    @Override
    public OcDingtalkDept queryOcDingtalkDeptByDeptId(Long deptId, String uid) {
        Example example = new Example(OcDingtalkDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deptId", deptId);
        criteria.andEqualTo("dingtalkUid", uid);
        return ocDingtalkDeptMapper.selectOneByExample(example);
    }

    @Override
    public List<OcDingtalkDept> queryDingtalkRootDept() {
        Example example = new Example(OcDingtalkDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", 0);
        return ocDingtalkDeptMapper.selectByExample(example);
    }
}
