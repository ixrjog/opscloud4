package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerTaskMember;
import com.baiyi.opscloud.mapper.opscloud.OcServerTaskMemberMapper;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/8 2:28 下午
 * @Version 1.0
 */
@Service
public class OcServerTaskMemberServiceImpl implements OcServerTaskMemberService {

    @Resource
    private OcServerTaskMemberMapper ocServerTaskMemberMapper;

    @Override
    public void addOcServerTaskMember(OcServerTaskMember ocServerTaskMember) {
        ocServerTaskMemberMapper.insert(ocServerTaskMember);
    }

    @Override
    public void updateOcServerTaskMember(OcServerTaskMember ocServerTaskMember) {
        ocServerTaskMemberMapper.updateByPrimaryKey(ocServerTaskMember);
    }

    @Override
    public OcServerTaskMember queryOcServerTaskMemberById(int id) {
        return ocServerTaskMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcServerTaskMember> queryOcServerTaskMemberByTaskStatus(int taskId, String taskStatus, int size) {
        Example example = new Example(OcServerTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("host_pattern");
        criteria.andEqualTo("taskId", taskId);
        criteria.andEqualTo("taskStatus", taskStatus);
        PageHelper.startPage(1, size);
        return ocServerTaskMemberMapper.selectByExample(example);
    }

    @Override
    public int countOcServerTaskMemberByTaskStatus(int taskId, String taskStatus, int size) {
        Example example = new Example(OcServerTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("host_pattern");
        criteria.andEqualTo("taskId", taskId);
        criteria.andEqualTo("taskStatus", taskStatus);
        PageHelper.startPage(1, size);
        return ocServerTaskMemberMapper.selectCountByExample(example);
    }

    @Override
    public List<OcServerTaskMember> queryOcServerTaskMemberByTaskId(int taskId){
        Example example = new Example(OcServerTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("host_pattern");
        criteria.andEqualTo("taskId", taskId);
        return ocServerTaskMemberMapper.selectByExample(example);
    }

}
