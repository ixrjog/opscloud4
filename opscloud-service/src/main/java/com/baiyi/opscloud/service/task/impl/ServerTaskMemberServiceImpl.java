package com.baiyi.opscloud.service.task.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.mapper.ServerTaskMemberMapper;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 5:32 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ServerTaskMemberServiceImpl implements ServerTaskMemberService {

    private final ServerTaskMemberMapper serverTaskMemberMapper;

    @Override
    public ServerTaskMember getById(Integer id) {
        return serverTaskMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(ServerTaskMember serverTaskMember) {
        serverTaskMemberMapper.insert(serverTaskMember);
    }

    @Override
    public int countByTaskStatus(Integer serverTaskId, String taskStatus) {
        Example example = new Example(ServerTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverTaskId", serverTaskId)
                .andEqualTo("taskStatus", taskStatus);
        return serverTaskMemberMapper.selectCountByExample(example);
    }

    @Override
    public int countByFinalized(Integer serverTaskId, boolean finalized) {
        Example example = new Example(ServerTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverTaskId", serverTaskId)
                .andEqualTo("finalized", finalized);
        return serverTaskMemberMapper.selectCountByExample(example);
    }

    @Override
    public void update(ServerTaskMember serverTaskMember) {
        serverTaskMemberMapper.updateByPrimaryKey(serverTaskMember);
    }

    @Override
    public List<ServerTaskMember> queryByServerTaskId(Integer serverTaskId) {
        Example example = new Example(ServerTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverTaskId", serverTaskId);
        return serverTaskMemberMapper.selectByExample(example);
    }

}