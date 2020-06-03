package com.baiyi.opscloud.service.serverChange.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.mapper.opscloud.OcServerChangeTaskFlowMapper;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskFlowService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/28 1:24 下午
 * @Version 1.0
 */
@Service
public class OcServerChangeTaskFlowServiceImpl implements OcServerChangeTaskFlowService {

    @Resource
    private OcServerChangeTaskFlowMapper ocServerChangeTaskFlowMapper;

    @Override
    public List<OcServerChangeTaskFlow> queryOcServerChangeTaskFlowByTaskId(String taskId) {
        Example example = new Example(OcServerChangeTaskFlow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        return ocServerChangeTaskFlowMapper.selectByExample(example);
    }

    @Override
    public OcServerChangeTaskFlow queryOcServerChangeTaskFlowById(int id) {
        return ocServerChangeTaskFlowMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcServerChangeTaskFlow queryOcServerChangeTaskFlowByParentId(int parentId) {
        Example example = new Example(OcServerChangeTaskFlow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("flowParentId", parentId);
        PageHelper.startPage(1, 1); // limit 1
        return ocServerChangeTaskFlowMapper.selectOneByExample(example);
    }

    @Override
    public void addOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlowMapper.insert(ocServerChangeTaskFlow);
    }

    @Override
    public void updateOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlowMapper.updateByPrimaryKey(ocServerChangeTaskFlow);
    }
}
