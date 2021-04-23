package com.baiyi.opscloud.service.dingtalk.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkUserDept;
import com.baiyi.opscloud.mapper.opscloud.OcDingtalkUserDeptMapper;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkUserDeptService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/9 2:56 下午
 * @Since 1.0
 */

@Service
public class OcDingtalkUserDeptServiceImpl implements OcDingtalkUserDeptService {

    @Resource
    private OcDingtalkUserDeptMapper ocDingtalkUserDeptMapper;

    @Override
    public void addOcDingtalkUserDeptList(List<OcDingtalkUserDept> ocDingtalkUserDeptList) {
        ocDingtalkUserDeptMapper.insertList(ocDingtalkUserDeptList);
    }

    @Override
    public void delOcDingtalkUserDeptByDeptId(Integer ocDingtalkDeptId) {
        Example example = new Example(OcDingtalkUserDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ocDingtalkDeptId", ocDingtalkDeptId);
        ocDingtalkUserDeptMapper.deleteByExample(example);
    }

    @Override
    public List<OcDingtalkUserDept> queryOcDingtalkUserDeptByDeptId(Integer ocDingtalkDeptId) {
        Example example = new Example(OcDingtalkUserDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ocDingtalkDeptId", ocDingtalkDeptId);
        return ocDingtalkUserDeptMapper.selectByExample(example);
    }
}
